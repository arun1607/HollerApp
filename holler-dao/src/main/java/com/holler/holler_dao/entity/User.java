package com.holler.holler_dao.entity;

import com.holler.holler_dao.entity.enums.UserStatusType;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5464369837316338488L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "about")
	private String about;
	
	@Column(name = "pic")
	private String pic;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private UserStatusType status;
	
	@ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinTable(name = "user_tag",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id"))
	private Set<Tags> tags;

	@Column(name = "current_location")
	private String currentLocation;

	@Column(name = "current_address")
	private String currentAddress;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_setting_details_id")
	private UserSettingDetails userSettingDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	public Set<Tags> getTags() {
		return tags;
	}
	public void setTags(Set<Tags> tags) {
		this.tags = tags;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		this.status = status;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public UserSettingDetails getUserSettingDetails() {
		return userSettingDetails;
	}

	public void setUserSettingDetails(UserSettingDetails userSettingDetails) {
		this.userSettingDetails = userSettingDetails;
	}

	public static User constructUserForSignUp(String name, String email, String phoneNumber) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPhoneNumber(phoneNumber);
		user.setStatus(UserStatusType.ACTIVE);
		return user;
	}

	public Double[] getLatLongFromCurrentLocation() {
		Double[] latLong = new Double[2];
		String[] latLongString = this.currentLocation.split(",");
		latLong[0] = Double.parseDouble(latLongString[0]);
		latLong[1] = Double.parseDouble(latLongString[1]);
		return latLong;
	}

}
