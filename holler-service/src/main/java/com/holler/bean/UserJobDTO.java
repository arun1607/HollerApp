package com.holler.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.holler.holler_dao.entity.Jobs;
import com.holler.holler_dao.entity.User;
import com.holler.holler_dao.util.AddressConverter;
import com.holler.holler_dao.util.CommonUtil;


public class UserJobDTO {
	private Integer userId;
	private Integer jobId;
	private String title;
	private String jobDescription;
	private String status;
	private Set<Integer> tags;
	private Integer compensation;
	private Date jobTimeStamp;	//job created date
	private String specialrequirement;
	private int genderRequirement;
	private Date jobdate;	//date on which job should be done
	private String jobAddress;
	private double lat;
	private double lng;

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCompensation() {
		return compensation;
	}
	public void setCompensation(Integer compensation) {
		this.compensation = compensation;
	}
	public Date getJobTimeStamp() {
		return jobTimeStamp;
	}
	public void setJobTimeStamp(Date jobTimeStamp) {
		this.jobTimeStamp = jobTimeStamp;
	}
	public String getSpecialrequirement() {
		return specialrequirement;
	}
	public void setSpecialrequirement(String specialrequirement) {
		this.specialrequirement = specialrequirement;
	}
	public int getGenderRequirement() {
		return genderRequirement;
	}
	public void setGenderRequirement(int genderRequirement) {
		this.genderRequirement = genderRequirement;
	}
	public Set<Integer> getTags() {
		return tags;
	}
	public void setTags(Set<Integer> tags) {
		this.tags = tags;
	}

	public Date getJobdate() {
		return jobdate;
	}

	public void setJobdate(Date jobdate) {
		this.jobdate = jobdate;
	}
	
	public String getJobAddress() {
		return jobAddress;
	}
	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public static List<UserJobDTO> constructUserJobDTO(List<Object[]> userJobs){
		List<UserJobDTO> userJobDTOs = new ArrayList<UserJobDTO>();
		if(userJobs != null && !userJobs.isEmpty()){
			for (Object[] object : userJobs) {
				if(object != null){
					UserJobDTO userJobDTO = new UserJobDTO();
					userJobDTO.setTitle((String)object[1]);
					userJobDTO.setJobDescription((String)object[2]);
					userJobDTOs.add(userJobDTO);
				}
			}
		}
		return userJobDTOs;
	}

	public static Jobs constructJobToPost(UserJobDTO userJobDTO) {
		Jobs job = new Jobs();
		if(CommonUtil.isNotNull(userJobDTO.getJobId())){
			job.setId(userJobDTO.getJobId());
		}
		job.setTitle(userJobDTO.getTitle());
		job.setDescription(userJobDTO.getJobDescription());
		job.setCompensation(userJobDTO.getCompensation());
		job.setGenderPreference(userJobDTO.getGenderRequirement());
		job.setSpecialRequirement(userJobDTO.getSpecialrequirement());
		job.setJobDate(userJobDTO.getJobdate());
		job.setStatus(userJobDTO.getStatus());
		job.setJobLocation(getLocationInCommaSeparatedString(userJobDTO));
		job.setJobAddress(getAddressFromLocation(userJobDTO));
		return job;
	}

	public static UserJobDTO getJobDtoFromJob(Jobs job){
		UserJobDTO jobDTO = new UserJobDTO();
		jobDTO.setUserId(job.getUser().getId());
		jobDTO.setJobId(job.getId());
		jobDTO.setTitle(job.getTitle());
		jobDTO.setJobDescription(job.getDescription());
		jobDTO.setCompensation(job.getCompensation());
		jobDTO.setSpecialrequirement(job.getSpecialRequirement());
		jobDTO.setGenderRequirement(job.getGenderPreference());
		jobDTO.setJobAddress(job.getJobAddress());
		jobDTO.setJobdate(job.getJobDate());
		return jobDTO;
	}

	public static List<UserJobDTO> getJobIdAndTitleDtosFromJobs(List<Jobs> jobs){
		List<UserJobDTO> jobDTOs = new ArrayList<UserJobDTO>();
		for (Jobs job : CommonUtil.safe(jobs)) {
			UserJobDTO userJobDTO = new UserJobDTO();
			userJobDTO.setJobId(job.getId());
			userJobDTO.setTitle(job.getTitle());
			jobDTOs.add(userJobDTO);
		}
		return jobDTOs;
	}
	
	public static List<UserJobDTO> getJobDtosToViewJobList(List<Jobs> jobs){
		List<UserJobDTO> jobDTOs = new ArrayList<UserJobDTO>();
		for (Jobs job : CommonUtil.safe(jobs)) {
			UserJobDTO userJobDTO = new UserJobDTO();
			userJobDTO.setJobId(job.getId());
			userJobDTO.setTitle(job.getTitle());
			userJobDTO.setCompensation(job.getCompensation());
			userJobDTO.setJobdate(job.getJobDate());
			jobDTOs.add(userJobDTO);
		}
		return jobDTOs;
	}


	public static List<UserJobDTO> getJobIdAndTitleByDiscoveryPreference(List<Jobs> jobs, User user) {
		int jobDiscoveryLimit = user.getJobDiscoveryLimit();
		Double[] userLatLong = user.getLatLongFromCurrentLocation();
		List<UserJobDTO> jobDTOs = new ArrayList<UserJobDTO>();
		for (Jobs job : CommonUtil.safe(jobs)) {
			Double[] jobLatLong = job.getLatLongFromJobLocation();
			Double userAndJobDistance = AddressConverter.calculateDistanceUsingLatLong(userLatLong[0], userLatLong[1], jobLatLong[0], jobLatLong[1]);
			if (userAndJobDistance <= jobDiscoveryLimit) {
				UserJobDTO userJobDTO = new UserJobDTO();
				userJobDTO.setJobId(job.getId());
				userJobDTO.setTitle(job.getTitle());
				userJobDTO.setCompensation(job.getCompensation());
				userJobDTO.setJobdate(job.getJobDate());
				jobDTOs.add(userJobDTO);
			}
		}
		return jobDTOs;
	}

	public static String getLocationInCommaSeparatedString(UserJobDTO userJobDTO) {
		StringBuilder locationBuilder = new StringBuilder();
		locationBuilder.append(String.valueOf(userJobDTO.getLat())).append(",").append(String.valueOf(userJobDTO.getLng()));
		return locationBuilder.toString();
	}

	public static String getAddressFromLocation(UserJobDTO userJobDTO){
		return AddressConverter.getAddressFromLatLong(getLocationInCommaSeparatedString(userJobDTO));
	}

}
