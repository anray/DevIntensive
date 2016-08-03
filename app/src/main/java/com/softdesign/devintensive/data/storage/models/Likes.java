package com.softdesign.devintensive.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


/**
 * Created by anray on 28.07.2016.
 */
@Entity(active = true, nameInDb = "LIKES", indexes = {
        @Index(value = "userIdWhoLiked, userRemoteId", unique = true)})
public class Likes {

    @Id
    private Long Id;

    @NotNull
    private String userIdWhoLiked;

    private String userRemoteId;

    /** Used for active entity operations. */
    @Generated(hash = 683253613)
    private transient LikesDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    public Likes(String like, String userRemoteId) {
        userIdWhoLiked = like;
        this.userRemoteId = userRemoteId;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1008259696)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLikesDao() : null;
    }


    public String getUserRemoteId() {
        return this.userRemoteId;
    }


    public void setUserRemoteId(String userRemoteId) {
        this.userRemoteId = userRemoteId;
    }


    public String getUserIdWhoLiked() {
        return this.userIdWhoLiked;
    }


    public void setUserIdWhoLiked(String userIdWhoLiked) {
        this.userIdWhoLiked = userIdWhoLiked;
    }


    public Long getId() {
        return this.Id;
    }


    public void setId(Long Id) {
        this.Id = Id;
    }


    @Generated(hash = 677741012)
    public Likes(Long Id, @NotNull String userIdWhoLiked, String userRemoteId) {
        this.Id = Id;
        this.userIdWhoLiked = userIdWhoLiked;
        this.userRemoteId = userRemoteId;
    }


    @Generated(hash = 1083466601)
    public Likes() {
    }
}
