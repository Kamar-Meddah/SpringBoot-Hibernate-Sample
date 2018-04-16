package com.meddah.kamar.springdemo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Date;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public boolean sendEmail(String to, String subject, String text, String html, String[] joint) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper( message, true );
            helper.setTo( to );
            helper.setSubject( subject );
            helper.setFrom( "demoSpring@demo.org" );
            helper.setSentDate( Date.from( Instant.now() ) );
            helper.setText( text, html );
            if (joint.length > 1) {
                helper.addAttachment( joint[0], new ClassPathResource( joint[1] ) );
            }
            this.emailSender.send( message );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper( message, false );
            helper.setTo( to );
            helper.setSubject( subject );
            helper.setFrom( "demoSpring@demo.org" );
            helper.setSentDate( Date.from( Instant.now() ) );
            helper.setText( text );
            this.emailSender.send( message );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendEmail(String to, String subject, String text, String html) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper( message, true );
            helper.setTo( to );
            helper.setSubject( subject );
            helper.setFrom( "demoSpring@demo.org" );
            helper.setSentDate( Date.from( Instant.now() ) );
            helper.setText( text, html );
            this.emailSender.send( message );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
