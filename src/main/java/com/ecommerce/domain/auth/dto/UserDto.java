package com.ecommerce.domain.auth.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String password;
    private String email;
    private String addressRoad;
    private String addressDetail;
    private String userRole;
    private LocalDateTime created_date;
    private LocalDateTime modified_date;
    private Boolean deleted_status;

    public UserDto(Long userId, String password, String addressRoad, String addressDetail, String userRole) {
        this.userId = userId;
        this.password = password;
        this.addressRoad = addressRoad;
        this.addressDetail = addressDetail;
        this.userRole = userRole;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getAddressRoad() {
		return addressRoad;
	}

	public void setAddressRoad(String addressRoad) {
		this.addressRoad = addressRoad;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getModified_date() {
		return modified_date;
	}

	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}

	public Boolean getDeleted_status() {
		return deleted_status;
	}

	public void setDeleted_status(Boolean deleted_status) {
		this.deleted_status = deleted_status;
	}
    

}
