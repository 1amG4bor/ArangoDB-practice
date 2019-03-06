package com.epam.arango_practice.model;

import com.arangodb.entity.DocumentEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Represent the family-member model
 */
@Api(value = "/member", description = "Operations about members of the family")
public class Member extends DocumentEntity {
	@NotNull
	@ApiModelProperty(value = "Full name", required = true, example = "\"John Smith\"")
	private String name;

	@NotNull
	@ApiModelProperty(value = "Date of birth", required = true, example = "\"1980-01-01\"")
	private String birthDate;

	@NotNull
	@ApiModelProperty(value = "Type of gender", required = true, example = "\"MALE\"", allowableValues = "MALE,FEMALE", dataType = "Gender")
	private Gender gender;

	public Member() {
	}

	public Member(String name, String birthDate, Gender gender) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public Gender getGender() {
		return this.gender;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Member) {
			return this.name.equals(((Member) obj).getName())
							&& this.birthDate.equals(((Member) obj).getBirthDate())
							&& this.gender.equals(((Member) obj).getGender());
		}
		return false;
	}
}
