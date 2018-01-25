package com.company.email;


import javax.mail.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Need to choose logger (https://habrahabr.ru/post/247647/) and use it
// TODO: Add patient welcome mail check method

/**
 * The NotificationReader class provides methods to get:
 * 1. verification code from email
 * 2. password reminder from email
 *
 * All notifications sends from ["notifications@company.com" <tester@company.com>]
 */
public class NotificationsReader {
    private Logger log = Logger.getLogger(NotificationsReader.class.getName());

    private String LETTER_PASSWORD_REMINDER = "Password reminder from company.com";
    private String LETTER_VERIFICATION_CODE = "Email verification from company.com";
    private String LETTER_WELCOME = "Patient Welcome";
    private String LETTER_PRIVATE = "New private message on company.com";

    private String PATTERN_TEMP_PASSWORD = "(Your temporary password is )(\\w{5}-\\w{8})";
    private String PATTERN_VERIFICATION_CODE = "(Your verification code is )(\\d{5})";

    private Properties property;

    private String host;
    private String username;
    private String password;

    private Session session = null;
    private Store store = null;

    private int CHECK_MAILS_COUNT = 25;

    /**
     * Class constructor
     */
    public NotificationsReader() throws RuntimeException
    {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/mailbox.properties");
            property = new Properties();
            property.load(fis);

            host = property.getProperty("mail.host");
            username = property.getProperty("mail.username");
            password = property.getProperty("mail.password");
            log.log(Level.INFO, "Login into: {0}" , username);

            if (host.isEmpty())
                throw new RuntimeException("Host cannot be empty\nPlease check mailbox.properties file");

            if (username.isEmpty() || password.isEmpty())
                throw new RuntimeException("Username or/and password cannot be empty!\nPlease check mailbox.properties file");

            session = Session.getDefaultInstance(property, null);
            store = session.getStore("imaps");
            store.connect(host, username, password);

        } catch (FileNotFoundException e)
        {
            log.log(Level.SEVERE, "File mailbox.properties not found!\n{0}", e.getMessage());

        } catch (IOException e)
        {
            log.log(Level.SEVERE, "Cannot open/read file mailbox.properties\n{0}", e.getMessage());
        } catch (NoSuchProviderException e)
        {
            log.log(Level.SEVERE, "Cannot use IMAP to read emails!\n{0}", e.getMessage());
        } catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check message subject
     * @param m javax.mail.Message object
     * @param patternSubject subject to compare with
     * @return True if Message subject equal to Pattern Subject
     * @throws MessagingException
     */
    private boolean isMessageSubject(Message m, String patternSubject) throws MessagingException
    {
        return m.getSubject().equals(patternSubject);
    }

    /**
     * Compare message sender with pattern
     * @param m javax.mail.Message object
     * @param sender seder to compare to
     * @return True if message sender = sender
     * @throws MessagingException
     */
    private boolean checkSender(Message m, String sender) throws MessagingException
    {
        // Could be multiple senders (one from SMTP protocol and second from Mime)
        // Should check both
        StringBuilder sb = new StringBuilder();
        for (Address s: m.getFrom()) {
            sb.append(s.toString());
            sb.append("; ");
        }

        log.log(Level.FINEST, "Senders: {0}", sb.toString());
        return sb.toString().contains(sender);
    }

    /**
     * Compare recipient with pattern
     * @param m javax.mail.Message object
     * @param recipient recipient to compare to
     * @return True if recipient in list
     * @throws MessagingException
     */
    private boolean checkRecipient(Message m, String recipient) throws MessagingException
    {
        // Possible error if recipient will be added to CC or BCC field
        Address[] rTo = m.getRecipients(Message.RecipientType.TO);

        for (Address r: rTo) {
            if (r.toString().contains(recipient))
                return true;
        }
        return false;
    }

    /**
     * Get Multipart message body as string (only TEXT/HTML parts)
     * @param m javax.mail.message
     * @return Message body as string
     * @throws MessagingException
     */
    private String getMessageBody(Message m) throws MessagingException
    {
        try {
            Object msgContent = m.getContent();

            if (msgContent instanceof Multipart) {
                Multipart multipart = (Multipart) msgContent;

                StringBuilder body = new StringBuilder();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bp = multipart.getBodyPart(i);
                    // TODO: Possible error if email template will be change
                    if (bp.getContentType().contains("TEXT/HTML"))
                        body.append(bp.getContent());
                }

                return body.toString();
            }
            else
            {
                log.log(Level.WARNING, "Not a Multipart message!");
                return "";
            }
        } catch (IOException e)
        {
            log.severe(e.getMessage());
        }
        return "";
    }

    /**
     * Get verification code for new patient
     * @param patientEmail patient email
     * @return verification code
     */
    public String getVerificationCode(String patientEmail)
    {
        try {
            Folder inbox = store.getFolder(property.getProperty("mail.notification.folder.name"));
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();

            for (int mi = messageCount; mi > (messageCount - CHECK_MAILS_COUNT); mi--)
            {
                Message msg = inbox.getMessage(mi);
                if (isMessageSubject(msg, LETTER_VERIFICATION_CODE) &&
                        checkSender(msg, property.getProperty("mail.notification.sender"))) {
                    if (checkRecipient(msg, patientEmail)) {
                        log.log(Level.INFO, "Found verification letter for patient: {0}", patientEmail);
                        Pattern p = Pattern.compile(PATTERN_VERIFICATION_CODE);
                        Matcher m = p.matcher(getMessageBody(msg));

                        while (m.find()) {
                            return m.group(2);
                        }
                    }
                }
            }
        } catch (MessagingException e)
        {
            log.severe("Error while reading emails!\n" + e.getMessage());
        }
        log.log(Level.WARNING,"Verification letter for patient with email {0} not found!", patientEmail);
        return "NO_VERIFICATION_CODE_FOUND";
    }

    /**
     * Get Temporary password for patient
     * @param patientEmail patient email
     * @return temporary password
     */
    public String getPasswordReminder(String patientEmail)
    {
        try {
            Folder inbox = store.getFolder(property.getProperty("mail.notification.folder.name"));
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();

            for (int mi = messageCount; mi > (messageCount - CHECK_MAILS_COUNT); mi--)
            {
                Message msg = inbox.getMessage(mi);
                if (isMessageSubject(msg, LETTER_PASSWORD_REMINDER) &&
                        checkSender(msg, property.getProperty("mail.notification.sender"))) {
                    if (checkRecipient(msg, patientEmail)) {
                        log.log(Level.INFO, "Found password reminder letter for patient: {0}", patientEmail);
                        Pattern p = Pattern.compile(PATTERN_TEMP_PASSWORD);
                        Matcher m = p.matcher(getMessageBody(msg));

                        while (m.find()) {
                            return m.group(2);
                        }
                    }
                }
            }
        } catch (MessagingException e)
        {
            log.severe("Error while reading emails!\n" + e.getMessage());
        }
        log.log(Level.WARNING,"Password reminder letter for patient with email {0} not found!", patientEmail);
        return "NO_PASSWORD_FOUND";
    }

    /**
     * Check welcome letter for new patient
     * @param patientEmail patient email
     * @return True if letter exist, else False
     */
    public boolean checkWelcomeLetter(String patientEmail)
    {
        try {
            Folder inbox = store.getFolder(property.getProperty("mail.notification.folder.name"));
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();

            for (int mi = messageCount; mi > (messageCount - CHECK_MAILS_COUNT); mi--)
            {
                Message msg = inbox.getMessage(mi);
                if (isMessageSubject(msg, LETTER_WELCOME) &&
                        checkSender(msg, property.getProperty("mail.notification.sender")) &&
                        checkRecipient(msg, patientEmail)) {
                    log.log(Level.INFO, "Found welcome letter for patient with email: {0}", patientEmail);
                    return true;
                }
            }
        } catch (MessagingException e)
        {
            log.severe("Error while reading emails!\n" + e.getMessage());
        }
        log.log(Level.WARNING,"Welcome letter for patient with email {0} not found!", patientEmail);
        return false;
    }

    /**
     * Check new private letter notifications
     * @param patientEmail patient email
     * @return True if email found, else False
     */
    public boolean checkPrivateMessage(String patientEmail)
    {
        // TODO: need refactoring!!! Copy-Paste from checkWelcomeLetter
        try {
            Folder inbox = store.getFolder(property.getProperty("mail.notification.folder.name"));
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();

            for (int mi = messageCount; mi > (messageCount - CHECK_MAILS_COUNT); mi--)
            {
                Message msg = inbox.getMessage(mi);
                if (isMessageSubject(msg, LETTER_PRIVATE) &&
                        checkSender(msg, property.getProperty("mail.notification.sender")) &&
                        checkRecipient(msg, patientEmail)) {
                    log.log(Level.INFO, "Found private letter for patient with email: {0}", patientEmail);
                    return true;
                }
            }
        } catch (MessagingException e)
        {
            log.severe("Error while reading emails!\n" + e.getMessage());
        }
        log.log(Level.WARNING,"Private letter for patient with email {0} not found!", patientEmail);
        return false;

    }
}
