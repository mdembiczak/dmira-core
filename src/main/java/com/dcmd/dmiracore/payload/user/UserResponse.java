package com.dcmd.dmiracore.payload.user;

import java.util.Set;

public class UserResponse {
    private String id;
    private String username;
    private String email;
    private Set<String> roles;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }


    public static final class Builder {
        private String id;
        private String username;
        private String email;
        private Set<String> roles;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserResponse build() {
            UserResponse userResponse = new UserResponse();
            userResponse.roles = this.roles;
            userResponse.id = this.id;
            userResponse.email = this.email;
            userResponse.username = this.username;
            return userResponse;
        }
    }
}
