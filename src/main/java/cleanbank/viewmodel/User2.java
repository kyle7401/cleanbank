package cleanbank.viewmodel;

// egov 참고용 소스

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by hyoseop on 2015-11-06.
 */
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    /*@OneToMany(mappedBy = "user")
    private Set<UserRole> roles;*/

    public static User2 createUser(String username, String email, String password) {
        User2 user = new User2();

        user.username = username;
        user.email = email;
        user.password = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);

       /* if(user.roles == null) {
            user.roles = new HashSet<UserRole>();
        }

        //create a new user with basic user privileges
        user.roles.add(
                new UserRole(
//                        RoleEnum.USER.toString(),
                        "USER",
                        user
                ));*/

        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

/*    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }*/
}