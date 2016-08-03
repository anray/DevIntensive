package com.softdesign.devintensive.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anray on 01.08.2016.
 */
public class UserLikeResponse {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("homeTask")
        @Expose
        public int homeTask;
        @SerializedName("projects")
        @Expose
        public int projects;
        @SerializedName("linesCode")
        @Expose
        public int linesCode;
        @SerializedName("likesBy")
        @Expose
        public List<String> likesBy = new ArrayList<String>();
        @SerializedName("rait")
        @Expose
        public int rait;
        @SerializedName("updated")
        @Expose
        public String updated;
        @SerializedName("rating")
        @Expose
        public int rating;

        public List<String> getLikesBy() {
            return likesBy;
        }

        public int getRait() {
            return rait;
        }

        public int getRating() {
            return rating;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getProjects() {
            return projects;
        }
    }
}
