package com.holler.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.holler.holler_dao.entity.Jobs;


public class UserJobDTO {
	private int userId;
	private int jobId;
	private String title;
    private String jobDescription;
    private String status;
    private Integer tags;
    private Integer compensation;
    private Date jobTimeStamp;
    private String specialrequirement;
    private int genderRequirement;
    public int getUserId() {
		return userId;
	}
    public void setUserId(int userId) {
		this.userId = userId;
	}
    public int getJobId() {
		return jobId;
	}
    public void setJobId(int jobId) {
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
	/*public Set<Integer> getTags() {
		return tags;
	}
	public void setTags(Set<Integer> tags) {
		this.tags = tags;
	}*/
	
	public Integer getTags() {
		return tags;
	}
	public void setTags(Integer tags) {
		this.tags = tags;
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
		job.setTitle(userJobDTO.getTitle());
		job.setDescription(userJobDTO.getJobDescription());
		job.setCompensation(userJobDTO.getCompensation());
		job.setGenderPreference(userJobDTO.getGenderRequirement());
		job.setSpecialRequirement(userJobDTO.getSpecialrequirement());
		job.setStatus(userJobDTO.getStatus());
		//job.setCreated(new Date());
		return job;
	}
}
