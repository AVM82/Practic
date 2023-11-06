package com.group.practic.dto;


public class EmailDto {

    private String recipient;

    private String msgBody;

    private String subject;

    private String attachment;


    public EmailDto() {
    }


    public EmailDto(String recipient, String msgBody, String subject, String attachment) {
        super();
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
    }


    public String getRecipient() {
        return recipient;
    }


    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


    public String getMsgBody() {
        return msgBody;
    }


    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getAttachment() {
        return attachment;
    }


    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

}
