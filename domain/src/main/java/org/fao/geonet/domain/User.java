package org.fao.geonet.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import jeeves.guiservices.session.JeevesUser;
import jeeves.interfaces.Profile;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * A user entity. A user is used in spring security, controlling access to metadata as well as in the {@link jeeves.server.UserSession}.
 * 
 * @author Jesse
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "users")
public class User implements JeevesUser {
    private static final long serialVersionUID = 2589607276443866650L;

    int _id;
    String _username;
    String _surname;
    String _name;
    Set<String> _email = new HashSet<String>();
    Set<Address> _addresses = new HashSet<Address>();
    String _organisation;
    String _kind;
    Profile _profile;
    UserSecurity _security;

    /**
     * Get the userid.   This is a generated value and as such new instances should not have this set as it will simply be ignored
     * and could result in reduced performance.
     * 
     * @return the user id
     */
    @Id
    @GeneratedValue
    public int getId() {
        return _id;
    }

    /**
     * Set the userid.   This is a generated value and as such new instances should not have this set as it will simply be ignored
     * and could result in reduced performance.
     * @param id the userid
     * @return this user object
     */
    public @Nonnull User setId(int id) {
        this._id = id;
        return this;
    }

    /**
     * Get the username.  This is both required and must be unique
     * 
     * @return the username
     */
    @Column(nullable = false, unique=true)
    public @Nonnull String getUsername() {
        return _username;
    }

    /**
     * Set the username.  This is both required and must be unique
     * @param username the username.  This is both required and must be unique
     * @return this user object
     */
    public @Nonnull User setUsername(@Nonnull String username) {
        this._username = username;
        return this;
    }
    /**
     * Get the user's hashed password.  Actual passwords are not stored only hashes of the passwords.
     */
    @Transient
    @Override
    public String getPassword() {
        return new String(getSecurity().getPassword());
    }

    /**
     * Get the Surname/lastname of the user.  May be null
     * 
     * @return the Surname/lastname of the user.  May be null
     */
    public @Nullable String getSurname() {
        return _surname;
    }

    /**
     * Set the Surname/lastname of the user.  May be null
     * @param surname the Surname/lastname of the user.  May be null
     * @return this user object
     */
    public @Nonnull User setSurname(@Nullable String surname) {
        this._surname = surname;
        return this;
    }

    /**
     * Get the user's actual first name.  May be null.
     * @return the user's actual first name.  May be null.
     */
    public @Nullable String getName() {
        return _name;
    }

    /**
     * Set the user's actual first name.  May be null.
     * @param name the user's actual first name.  May be null.
     * @return this user object
     */
    public @Nonnull User setName(@Nullable String name) {
        this._name = name;
        return this;
    }

    @Override
    @Transient
    public String getEmail() {
        if (_email != null) {
            return _email.iterator().next();
        }
        return null;
    }

    /**
     * Get all the user's email addresses.
     *
     * @return the user's email addresses.
     */
    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @CollectionTable(name = "email")
    @Column(name = "email")
    public Set<String> getEmailAddresses() {
        return _email;
    }

    /**
     * Set all the email addresses.
     *
     * @param email all the email addresses.
     */
    protected void setEmailAddresses(Set<String> email) {
        this._email = email;
    }

    /**
     * Get all the user's addresses.
     * 
     * @return all the user's addresses.
     */ 
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "USER_ADDRESS", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = { @JoinColumn(name = "addressid", referencedColumnName = "ID", unique = true) })
    public Set<Address> getAddresses() {
        return _addresses;
    }

    /**
     * Set all the user's addresses.
     * @param addresses all the user's addresses.
     * @return this user object
     */
    protected User setAddresses(Set<Address> addresses) {
        this._addresses = addresses;
        return this;
    }
    
    /**
     * Get the first address in the list of the addresses.
     * 
     * @return the first address in the list of the addresses.
     */
    @Transient
    public @Nonnull Address getPrimaryAddress() {
        Set<Address> addresses = getAddresses();
        
        if (addresses.isEmpty()) {
            addresses.add(new Address());
        }
        
        return addresses.iterator().next();
    }

    /**
     * Return the organization the user is a part of.
     *
     * @return the user's organization.
     */
    public String getOrganisation() {
        return _organisation;
    }

    public User setOrganisation(String organization) {
        this._organisation = organization;
        return this;
    }

    /**
     * Get the 'kind' of user. Just a sting representing the type or category of the user. It can be customized for a particular
     * application. An example is GOV or CONTRACTOR.
     */
    @Column(length = 16)
    public String getKind() {
        return _kind;
    }

    /**
     * Set the 'kind' of user. Just a sting representing the type or category of the user. It can be customized for a particular
     * application. An example is GOV or CONTRACTOR.
     *
     * @param kind the 'kind' of user. Just a sting representing the type or category of the user. It can be customized for a particular
     * application. An example is GOV or CONTRACTOR.
     *
     * @return this user object
     */
    public @Nonnull User setKind(String kind) {
        this._kind = kind;
        return this;
    }

    /**
     * Get the user's profile. This is a required property.
     * 
     * @return the user's profile.
     */
    @Column(nullable = false)
    public @Nonnull Profile getProfile() {
        return _profile;
    }

    /**
     * Set the user's profile. This is a required property.
     *
     * @param profile the user's profile.
     *
     * @return this user object
     */
    public @Nonnull User setProfile(@Nonnull Profile profile) {
        this._profile = profile;
        return this;
    }

    /**
     * Get the object containing the information regarding security.
     *
     * @return the object containing the information regarding security.
     */
    public @Nonnull UserSecurity getSecurity() {
        return _security;
    }

    /**
     * Set the UserSecurity object.  It is to be used by JPA framework.
     *
     * @param security the security object
     * @return this user object
     */
    protected @Nonnull User setSecurity(@Nonnull UserSecurity security) {
        this._security = security;
        return this;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        if (_profile != null) {
            for (String p : getProfile().getAllNames()) {
                auths.add(new SimpleGrantedAuthority(p));
            }
        }
        return auths;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    /**
     * Merge all data from other user into this user.
     * 
     * @param otherUser other user to merge data from.
     * @param mergeNullData if true then also set null values from other user. If false then only merge non-null data
     */
    public void mergeUser(User otherUser, boolean mergeNullData) {
        if (mergeNullData || otherUser.getUsername() != null) {
            setUsername(otherUser.getUsername());
        }
        if (mergeNullData || otherUser.getSurname() != null) {
            setSurname(otherUser.getSurname());
        }
        if (mergeNullData || otherUser.getName() != null) {
            setName(otherUser.getName());
        }
        if (mergeNullData || otherUser.getOrganisation() != null) {
            setOrganisation(otherUser.getOrganisation());
        }
        if (mergeNullData || otherUser.getKind() != null) {
            setKind(otherUser.getKind());
        }
        if (mergeNullData || otherUser.getProfile() != null) {
            setProfile(otherUser.getProfile());
        }

        _email.clear();
        _email.addAll(otherUser.getEmailAddresses());

        ArrayList<Address> otherAddresses = new ArrayList<Address>(otherUser.getAddresses());

        for (Iterator<Address> iterator = _addresses.iterator(); iterator.hasNext();) {
            Address address = (Address) iterator.next();
            boolean found = false;

            for (Iterator<Address> iterator2 = otherAddresses.iterator(); iterator.hasNext();) {
                Address otherAddress = iterator2.next();
                if (otherAddress.getId() == address.getId()) {
                    address.mergeAddress(otherAddress, mergeNullData);
                    found = true;
                    iterator2.remove();
                    break;
                }
            }

            if (!found) {
                iterator.remove();
            }
        }

        _addresses.addAll(otherAddresses);
        getSecurity().mergeSecurity(otherUser.getSecurity(), mergeNullData);
    }
}