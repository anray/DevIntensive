package com.softdesign.devintensive.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anray on 20.07.2016.
 */
public class UserModelResponseByToken {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public class Repositories {

        @SerializedName("repo")
        @Expose
        public List<UserModelResponse.Repo> repo = new ArrayList<UserModelResponse.Repo>();
        @SerializedName("updated")
        @Expose
        public String updated;

        public List<UserModelResponse.Repo> getRepo() {
            return repo;
        }
    }
    public class Data {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("second_name")
        @Expose
        public String secondName;
        @SerializedName("__v")
        @Expose
        public int v;
        @SerializedName("repositories")
        @Expose
        public Repositories repositories;
        @SerializedName("contacts")
        @Expose
        public UserModelResponse.Contacts contacts;
        @SerializedName("profileValues")
        @Expose
        public UserModelResponse.ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        public UserModelResponse.PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        public String specialization;
        @SerializedName("role")
        @Expose
        public String role;
        @SerializedName("updated")
        @Expose
        public String updated;

        public String getFullName() {
            return firstName + " " + secondName;
        }

        public String getSecondName() {
            return secondName;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public UserModelResponse.PublicInfo getPublicInfo() {
            return publicInfo;
        }

        public UserModelResponse.ProfileValues getProfileValues() {
            return profileValues;
        }

        public String getId() {
            return id;
        }

        public UserModelResponse.Contacts getContacts() {
            return contacts;
        }

        public String getFirstName() {
            return firstName;
        }
    }


}
