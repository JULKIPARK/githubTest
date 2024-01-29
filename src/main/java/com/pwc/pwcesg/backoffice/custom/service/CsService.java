package com.pwc.pwcesg.backoffice.custom.service;
import com.pwc.pwcesg.backoffice.handler.FileHandler;
import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.custom.mapper.CsMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 문의 · 요청 관리 Service
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CsService {

	private final FileHandler fileHandler;

	@Value("${azure.communication.accesskey}")
	private String azureCommunicationAccesskey;

	private final CsMapper csMapper;

	/**
	 * 문의 · 요청 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectCsList(Map<String, Object> paramMap) {
		return csMapper.selectCsList(paramMap);
	}

	/**
	 * 문의/요청 관리 상세조회
	 *
	 * @param askUid
	 * @return
	 */
	public Map<String, Object> selectViewByCsDetailEditPop(String askUid) {
		return csMapper.selectViewByCsDetailEditPop(askUid);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int upsertAnsrInfoToAskUid(Map<String, Object> paramMap) {
		csMapper.updateAskInfoToAskStCdByAskUid(paramMap);

		MultipartFile uploadFile = (MultipartFile) paramMap.get("uploadFile");    // 컨텐츠업로드
		Map<String, Object> uploadFileMap = null;

		// 첨부파일 처리
		String gbn = "cs_files";
		log.info("gbn=>{}", gbn);
		log.info("uploadFile=>{}", uploadFile);
		if (uploadFile != null && !uploadFile.isEmpty()) {
			// 컨텐츠 파일저장
			uploadFileMap = fileHandler.parseFileInfo2(uploadFile, "0", gbn);
			paramMap.put("atacFileFpath", uploadFileMap.get("linkUrl"));
		}

		log.info("paramMap => {}", paramMap);

		int rtnValue = 0;
		if (paramMap.get("ansrUid").equals("")) {
			rtnValue = csMapper.insertAnsrInfo(paramMap);
		} else {
			rtnValue = csMapper.updateAnsrInfo(paramMap);
		}

		if (paramMap.get("askStCd").equals("50")) {
			// 답변메일 발송
			String MailContent = ""
				+ "		<div style=\"display:inline-block; padding:30px 30px;  width:100%; background:#f2f2f2; box-sizing:border-box;\">\r\n"
				+ "        <div style=\"width:100%; background:#fff;  background:#fff; padding:60px 3% 100px; box-sizing:border-box\">\r\n"
				+ "        	<div style=\"width:160px; display:inline-block;\"><img src=\"https://www.samilesg.com/frontoffice/img/common/logo_new.png\" style=\"width:100%;\"></div>\r\n"
				+ "        	<div style=\"width:100%; padding:100px 0p 80px; border-bottom:1px solid #dedede; margin-top:30px;\">\r\n"
				+ "        		<div style=\"font-size:36px; font-family:'NotoB'; text-align:center; color:#2d2d2d; margin-top:30px;\">온라인 문의∙요청 답변 안내</div>\r\n"
				+ "        		<div style=\"font-size:20px; text-align:Center; color:#2d2d2d; margin-top:32px; line-height:32px; margin-bottom:60px\">\r\n"
				+ "			"+paramMap.get("fstInsMbrNm")+"님, 회원님께서 <span style=\"color:#D04A02\">" + paramMap.get("fstInsDt") + "</span>에 문의∙요청하신 내용에 대한 답변이 등록되었습니다.<br/>답변은 온라인 문의 ∙ 요청 내역에서 확인하실 수 있습니다. 보다 더 나은 Samil ESG가 될 수 있도록 꾸준히 노력하겠습니다.\r\n"
//				+ "			회원님께서 문의∙요청하신 내용에 대한 답변이 등록되었습니다.<br/>답변은 온라인 문의 ∙ 요청 내역에서 확인하실 수 있습니다. 보다 더 나은 Samil ESG가 될 수 있도록 꾸준히 노력하겠습니다.\r\n"
				+ "        		</div>\r\n"
				+ "        	</div>\r\n"
				+ "        	<div style=\"width:100%; padding:60px 0px; border-bottom:1px solid #dedede\">\r\n"
				+ "        		<div style=\"width:100%; padding:40px; background:#f2f2f2; box-sizing:border-box; margin-top:24px;\">\r\n"
//				+ "					안녕하세요, Samil ESG입니다.<br>삼일PwC에서 주최한 ‘실무자가 알아야 할 ESG 공시 핵심 항목’ 교육에 관심 가져주셔서 감사드립니다.<br>요청주신 교육자료 첨부하여 송부드립니다.<br><br>"
//				+ "					다운로드 : <a href=\"https://samilesg.blob.core.windows.net/provision/download/실무자가 알아야 할 ESG 공시 핵심 항목.zip\" target=\"_download\">실무자가 알아야 할 ESG 공시 핵심 항목.zip</a>"
				+ "        			<span style=\"width:100%; display:flex;\">\r\n"
				+ "        				<div style=\"width:25%; font-size:16px; color:#464646\">문의유형</div>\r\n"
				+ "        				<div style=\"width:75%; font-size:16px; text-align:Right; font-family:'NotoB'; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;\">게시 자료에 대한 문의</div>\r\n"
				+ "        			</span>\r\n"
				+ "        			<span style=\"width:100%; display:flex; margin-top:24px;\">\r\n"
				+ "        				<div style=\"width:25%; font-size:16px; color:#464646\">문의제목</div>\r\n"
				+ "        				<div style=\"width:75%; font-size:16px; text-align:Right; font-family:'NotoB'; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;\">" + paramMap.get("askTtl") + "</div>\r\n"
				+ "        			</span>\r\n"
				+ "        			<span style=\"width:100%; display:flex; margin-top:24px;\">\r\n"
				+ "        				<div style=\"width:25%; font-size:16px; color:#464646\">작성일</div>\r\n"
				+ "        				<div style=\"width:75%; font-size:16px; text-align:Right; font-family:'NotoB'; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;\">" + paramMap.get("fstInsDt") + "</div>\r\n"
				+ "        			</span>\r\n"
				+ "        			<span style=\"width:100%; display:flex; margin-top:24px;\">\r\n"
				+ "        				<div style=\"width:25%; font-size:16px; color:#464646\">문의내용</div>\r\n"
				+ "        				<div style=\"width:75%; font-size:16px; text-align:Right; font-family:'NotoB'; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;\">" + (paramMap.get("askCn").toString().replace("\r\n", "<br>")).replace("\n", "<br>") + "</div>\r\n"
				+ "        			</span>\r\n"
				+ "        		</div>\r\n"
				+ "        		<div style=\"width:100%; text-align:left; font-size:12px; margin-top:32px; color:#2d2d2d; line-height:26px;\">\r\n"
				+ "			만약, 답변내용이 불충분하시다면 다시 한번 온라인 문의를 남겨주세요.<br/>최대한 빠르고 정확한 답변을 드릴 수 있도록 하겠습니다.\r\n"
//				+ "			* 본 교육자료는 삼일회계법인의 저작물로써 저작권법에 의해 보호받고 있습니다. 본 자료의 내용을 인용하거나 비영리적 목적으로 사용하실 경우에도 삼일회계법인과의 서면 계약을 통하여 출처를 밝힌 후 사용하실 수 있습니다.<br>"
//				+ "			허가없이 전재, 변조, 복사, 양도, 배포, 출판, 전시, 판매하거나 상품제작, 인터넷 및 데이터 베이스를 비롯한 각종 정보서비스 등에 사용하는 것을 금지합니다.<br>"
//				+ "			**본 교육자료의 작성자 또는 발행자는 ESG 관련 정보제공에 대하여 어떠한 책임이 없으며, 이용자가 본 자료의 정보를 사용하여 발생한 결과에 대하여 어떠한 보증도 하지 않습니다.\r\n"
				+ "				</div>\r\n"
				+ "        		<div style=\"width:380px; background:#D04A02; padding:19px 24px; box-sizing:border-box; color:#fff; font-size:18px; margin:24px auto 0; text-align:center;\">\r\n"
				+ "        			<a href=\"https://www.samilesg.com/qna/qnaListView\" style=\"color:#fff;\">온라인 문의 ∙ 요청 답변 확인하기</a>\r\n"
				+ "        		</div>\r\n"
				+ "        	</div>\r\n"
				+ "        	<div style=\"width:100%; padding-top:80px;\">\r\n"
				+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px;\">본 메일은 발신전용 메일이며 회신 처리되지 않습니다.</div>\r\n"
				+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px; margin-top:16px;\">© 2023 PwC Korea. All rights reserved.</div>\r\n"
				+ "        	</div>\r\n"
				+ "        </div>\r\n"
				+ "    </div>\r\n";
			
			// Azure Portal 리소스에서 연결 문자열을 가져올 수 있습니다.
			String connectionString = "endpoint=https://samilesg-sendmail.korea.communication.azure.com/;accesskey=" + azureCommunicationAccesskey;

			EmailClient emailClient = new EmailClientBuilder().connectionString(connectionString).buildClient();
			EmailAddress toAddress = new EmailAddress((String)paramMap.get("askMail"));
			EmailMessage emailMessage = new EmailMessage().setSenderAddress("DoNotReply@samilesg.com").setToRecipients(toAddress).setSubject("[Samil ESG] 온라인 문의∙요청에 대한 답변이 등록되었습니다.").setBodyHtml(MailContent);
//			EmailAddress toAddress = new EmailAddress("kkabi71@gmail.com");
//			EmailMessage emailMessage = new EmailMessage().setSenderAddress("DoNotReply@samilesg.com").setToRecipients(toAddress).setSubject("[Samil ESG] \"실무자가 알아야 할 ESG 공시 핵심 항목\" 교육자료 송부").setBodyHtml(MailContent);

			SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);
			PollResponse<EmailSendResult> result = poller.waitForCompletion();
		}

		return rtnValue;
	}
}
