package net.datasa.web5.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    String memberId;			//사용자 아이디
    String memberPassword;		//비밀번호
    String memberName;			//이름
    String email;				//이메일주소
    String phone;				//전화번호
    String address;				//주소
    Boolean enabled;			//계정 상태 (true:정상, false:사용불가)
    String rolename;            //권한명 (ROLE_USER:일반회원, ROLE_ADMIN:관리자)
}
