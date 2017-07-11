package cleanbank.viewmodel;

import java.io.Serializable;

/**
 * Created by hyoseop on 2015-12-15.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String authority;
    private String picture;
    private Integer id;
    private int bnCd;

    public int getBnCd() {
        return bnCd;
    }

    public void setBnCd(int bnCd) {
        this.bnCd = bnCd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
