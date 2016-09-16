package com.holler.bean;

import com.holler.holler_dao.entity.Jobs;
import com.holler.holler_dao.entity.Rating;
import com.holler.holler_dao.entity.enums.JobStatusType;
import com.holler.holler_dao.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class UserRatingDTO {
	static final Logger log = LogManager.getLogger(UserRatingDTO.class.getName());

	private Integer toUserId;
	private Integer fromUserId;
	private String toUserName;
	private String toUserProfilePic;
	private String fromUserName;
	private String fromUserProfilePic;
	private int rating;
	private String feedback;
	private int jobId;
	private String jobDesignation;

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}


	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public void setToUserProfilePic(String toUserProfilePic) {
		this.toUserProfilePic = toUserProfilePic;
	}

	public String getToUserProfilePic() {
		return toUserProfilePic;
	}

	public void setFromUserProfilePic(String fromUserProfilePic) {
		this.fromUserProfilePic = fromUserProfilePic;
	}

	public String getFromUserProfilePic() {
		return fromUserProfilePic;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobDesignation() {
		return jobDesignation;
	}

	public void setJobDesignation(String jobDesignation) {
		this.jobDesignation = jobDesignation;
	}

	public static List<UserRatingDTO> constructUserDTOForRatingScreen(List<Object[]> resultList) {
		List<UserRatingDTO> userRatingDTOs = new ArrayList<UserRatingDTO>();
		if(resultList != null && !resultList.isEmpty()){
			for (Object[] object : resultList) {
				if(object != null){
					UserRatingDTO userRatingDTO = new UserRatingDTO();
					userRatingDTO.setToUserId((Integer) object[0]);
					userRatingDTO.setToUserName((String) object[1]);
					userRatingDTO.setToUserProfilePic((String) object[2]);
					userRatingDTO.setJobDesignation((String) object[3]);
					userRatingDTOs.add(userRatingDTO);
				}
			}
		}
		return userRatingDTOs;
	}

	public static List<UserRatingDTO> constructUserRatingsDTO(List<Object[]> resultList) {
		List<UserRatingDTO> userJobDTOs = new ArrayList<UserRatingDTO>();
		if(resultList != null && !resultList.isEmpty()){
			for (Object[] object : resultList) {
				if(object != null){
					UserRatingDTO userRatingDTO = new UserRatingDTO();
					userRatingDTO.setFromUserId((Integer) object[0]);
					userRatingDTO.setFromUserName((String) object[1]);
					userRatingDTO.setFromUserProfilePic((String) object[2]);
					userRatingDTO.setRating((Integer) object[3]);
					userRatingDTO.setFeedback((String) object[4]);
					userJobDTOs.add(userRatingDTO);
				}
			}
		}
		return userJobDTOs;
	}


	public static Rating constructRatingDTOToSave(UserRatingDTO userRatingDTO) {
		Rating rating = new Rating();
		rating.setObjectId(userRatingDTO.getJobId());
		rating.setRating(userRatingDTO.getRating());
		rating.setFeedback(userRatingDTO.getFeedback());
		return rating;
	}

	public static Float calculateAverageRatingForUser(List<Object[]> resultList) {
		Float avgRating = 0f;
		int totalRating = 0 ;
		int totalCount = 0;
		if(resultList != null && !resultList.isEmpty()){
			for (Object[] object : resultList) {
				if(object != null){
					totalCount++;
					totalRating = totalRating + (Integer) object[3];
				}
			}
		}
		if(totalCount != 0){
			avgRating = (float)totalRating/totalCount;
		}
		return avgRating;
	}
}
