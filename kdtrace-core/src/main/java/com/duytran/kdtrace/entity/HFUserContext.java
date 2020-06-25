package com.duytran.kdtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "hf_user_context")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class HFUserContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String account;

    private String affiliation;

    private String mspId;

    @Type(type ="org.hibernate.type.BinaryType")
    @Column(name = "encoded_private_key")
    private byte[] encodedPrivateKey;

    @Column(name = "certificate", length = 2048)
    private String certificate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @OneToOne(mappedBy = "hfUserContext", fetch = FetchType.LAZY)
    private User user;

    public HFUserContext id(Long id) {
        this.id = id;
        return this;
    }

    public HFUserContext name(String name) {
        this.name = name;
        return this;
    }

    public HFUserContext account(String account) {
        this.account = account;
        return this;
    }

    public HFUserContext affiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public HFUserContext mspId(String mspId) {
        this.mspId = mspId;
        return this;
    }

    public HFUserContext encodedPrivateKey(byte[] encodedPrivateKey) {
        this.encodedPrivateKey = encodedPrivateKey;
        return this;
    }

    public HFUserContext certificate(String certificate) {
        this.certificate = certificate;
        return this;
    }

    public HFUserContext user(User user) {
        this.user = user;
        return this;
    }
}
