package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("member")
public class MemberDTO {

    private String memId;					// 아이디
    private String memPw;					// 비밀번호
    private String memName;				    // 이름
    private String memBirth;				// 생년월일
    private String memGender;				// 성별
    private String memEmail;				// 이메일
    private String memPhone;				// 연락처

    private String memAddr;			// (우편번호) 주소, 상세주소
    private String memAddr1;				// 우편번호
    private String memAddr2;				// 주소
    private String memAddr3;				// 상세주소

    private String memPROFILENAME;		// 업로드 파일이름
    private MultipartFile memProfile;	// 업로드 파일

    private String memJoinDate;
    private String memIs;   // 회원 구분용(2: 정지회원 1: 회원, 0: 비회원)
}

