package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by anray on 18.07.2016.
 */

@Entity(active = true, nameInDb = "USERS")
public class User {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    private String photo;

    @NotNull
    @Unique
    private String fullName;

    @NotNull
    @Unique
    private String searchName;

    private int rating;

    private int finalRating;

    private int codeLines;

    private int projects;

    private String bio;

    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
    })
    private List<Repository> mRepositories;

    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
    })
    private List<Likes> mLikes;

    public User(UserListRes.UserData userRes) {

        remoteId = userRes.getId();
        photo = userRes.getPublicInfo().getPhoto();
        fullName = userRes.getFullName();
        searchName = userRes.getFullName().toUpperCase();
        rating = userRes.getProfileValues().getRaiting();
        finalRating = userRes.getProfileValues().getFinalRating();
        codeLines = userRes.getProfileValues().getLinesCode();
        projects = userRes.getProfileValues().getProjects();
        bio = userRes.getPublicInfo().getBio();

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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1361215124)
    public synchronized void resetMRepositories() {
        mRepositories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 958343163)
    public List<Repository> getMRepositories() {
        if (mRepositories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RepositoryDao targetDao = daoSession.getRepositoryDao();
            List<Repository> mRepositoriesNew = targetDao._queryUser_MRepositories(remoteId);
            synchronized (this) {
                if(mRepositories == null) {
                    mRepositories = mRepositoriesNew;
                }
            }
        }
        return mRepositories;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getProjects() {
        return this.projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getCodeLines() {
        return this.codeLines;
    }

    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }

    public int getRaiting() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return this.rating;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 584000357)
    public synchronized void resetMLikes() {
        mLikes = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1952367450)
    public List<Likes> getMLikes() {
        if (mLikes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LikesDao targetDao = daoSession.getLikesDao();
            List<Likes> mLikesNew = targetDao._queryUser_MLikes(remoteId);
            synchronized (this) {
                if(mLikes == null) {
                    mLikes = mLikesNew;
                }
            }
        }
        return mLikes;
    }

    public int getFinalRating() {
        return this.finalRating;
    }

    public void setFinalRating(int finalRating) {
        this.finalRating = finalRating;
    }

    @Generated(hash = 1835570242)
    public User(Long id, @NotNull String remoteId, String photo, @NotNull String fullName,
            @NotNull String searchName, int rating, int finalRating, int codeLines, int projects,
            String bio) {
        this.id = id;
        this.remoteId = remoteId;
        this.photo = photo;
        this.fullName = fullName;
        this.searchName = searchName;
        this.rating = rating;
        this.finalRating = finalRating;
        this.codeLines = codeLines;
        this.projects = projects;
        this.bio = bio;
    }

    @Generated(hash = 586692638)
    public User() {
    }

}
