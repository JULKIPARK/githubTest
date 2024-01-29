package com.pwc.pwcesg.backoffice.contents.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.pwc.pwcesg.backoffice.contents.mapper.ContentInfoMapper;
import com.pwc.pwcesg.backoffice.handler.FileHandler;
import com.pwc.pwcesg.common.mapper.CommonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentInfoService {

  private final ContentInfoMapper contentInfoMapper;
  private final CommonMapper commonMapper;
  private final FileHandler fileHandler;

  /**
   * 콘텐츠정보 목록 조회
   *
   * @param paramMap
   * @return
   */
  public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap) {
    return contentInfoMapper.selectContentList(paramMap);
  }

  /**
   * 콘텐츠정보 상세 조회
   *
   * @param paramMap
   * @return
   */
  public Map<String, Object> selectContentListView(Map<String, Object> paramMap) {
    return contentInfoMapper.selectContentListView(paramMap);
  }

  /**
   * 출처정보 목록 조회
   *
   * @param paramMap
   * @return
   */
  public List<Map<String, Object>> selectSrcList(Map<String, Object> paramMap) {
    return contentInfoMapper.selectSrcList(paramMap);
  }

  /**
   * 출처정보 상세 조회
   *
   * @param srcUid
   * @return
   */
  public Map<String, Object> selectSrcView(String srcUid) {
    return contentInfoMapper.selectSrcView(srcUid);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
      SQLException.class})
  public int upsertSrcView(Map<String, Object> paramMap) {
    if (paramMap.get("srcUidByUpsertPop").equals("")) {
      return contentInfoMapper.insertSrcInfo(paramMap);
    } else {
      return contentInfoMapper.updateSrcInfo(paramMap);
    }
  }

  public Map<String, Object> selectSrcDetailBySrcUid(String srcUid) {
    return contentInfoMapper.selectSrcDetailBySrcUid(srcUid);
  }

  public List<Map<String, Object>> selectContentBySrcUid(String srcUid) {
    return contentInfoMapper.selectContentBySrcUid(srcUid);
  }

  public int updateSrcInfoToSrcNmBySrcId(Map<String, Object> paramMap) {
    return contentInfoMapper.updateSrcInfoToSrcNmBySrcId(paramMap);
  }

  public List<Map<String, Object>> selectContentSearchPopList(Map<String, Object> paramMap) {
    return contentInfoMapper.selectContentSearchPopList(paramMap);
  }

  public List<Map<String, Object>> selectSrcSearchPopView(Map<String, Object> paramMap) {
    return contentInfoMapper.selectSrcSearchPopView(paramMap);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
  public String upsertByContRegPop(Map<String, Object> paramMap,
      MultipartFile uploadImage,
      MultipartFile uploadFile,
      List<MultipartFile> uploadLessons
  ) throws Exception {
    String contUid = "0";
    int retVal = 0;
    String atacFileUid = "";
    
    System.out.println("[등록방식]"+ paramMap.get("contRmthdCd"));
    
    if (paramMap.get("atacFileUid").equals("")) {
      Map<String, Object> grpMap = new HashMap<>();
      commonMapper.insertAtacFileInfo(grpMap);
      atacFileUid = grpMap.get("atacFileUid").toString();
    } else {
      atacFileUid = paramMap.get("atacFileUid").toString();
    }

    List<Map> atacDtlData = new ArrayList<>();
    List<Map> contApndData = new ArrayList<>();

    // todo 영상 업로드 등록시, 영상 링크 등록시 재생시간 등 가져오는 방법 확인 필요함.

    if ((paramMap.get("contKindCd").equals("10") || paramMap.get("contKindCd").equals("20")) && paramMap.get("contRmthdCd").equals("10")) {
      if (uploadFile.isEmpty() || uploadFile == null) {
        if (paramMap.get("uploadFileUid").equals("")) {
          //  !paramMap.get("uploadFileUid").equals("")
          log.error("valid error!!");
          throw new Exception("강제 예외 발생!!!");
        } else {
          Map contApndMap = new HashMap();

          contApndMap.put("contUid", paramMap.get("contUid"));
          contApndMap.put("contKindCd", paramMap.get("contKindCd"));
          contApndMap.put("seqNo", "1");
          contApndMap.put("contRmthdCd", paramMap.get("contRmthdCd"));
//                    contApndMap.put("linkUrl", paramMap.get("linkUrl"));
          contApndMap.put("nwinYn", "N");

          if (paramMap.get("contKindCd").equals("20")) {
            contApndMap.put("contSplyTpCd", "MP4");
            contApndMap.put("vodQuntyHh", paramMap.get("vodCtQuntyHh"));
            contApndMap.put("vodQuntyMi", paramMap.get("vodCtQuntyMi"));
            contApndMap.put("vodQuntySec", paramMap.get("vodCtQuntySec"));
          } else {
            contApndMap.put("contSplyTpCd", "PDF");
            contApndMap.put("pageCnt", "0");
          }

//                contApndMap.put("lesnNm", "");
//                contApndMap.put("vodComnt", "");
          contApndMap.put("atacFileUid", atacFileUid);
          contApndMap.put("atacDtlUid", paramMap.get("uploadFileUid"));
          contApndMap.put("fstInsId", "ADMIN");
          contApndMap.put("lstUpdId", "ADMIN");

          contApndData.add(contApndMap);
        }
      } else {
        if (paramMap.get("uploadFileUid").equals("")) {
          Map uploadedData = null;//fileHandler.parseFileInfo(uploadFile, atacFileUid, paramMap.get("contKindCd").toString());
          log.info("uploaded data ===> ", uploadedData);

          // todo issue : commonMapper.getAtacDtlUid() 서비스단을 벋어나지 않으면 commit 이 안되어 동일번호 채번됨...
          uploadedData.put("atacDtlUid", 50);

          atacDtlData.add(uploadedData);

          Map contApndMap = new HashMap();

          contApndMap.put("contUid", paramMap.get("contUid"));
          contApndMap.put("contKindCd", paramMap.get("contKindCd"));
          contApndMap.put("seqNo", "1");
          contApndMap.put("contRmthdCd", paramMap.get("contRmthdCd"));
//                    contApndMap.put("linkUrl", paramMap.get("linkUrl"));
          contApndMap.put("nwinYn", "N");

          if (paramMap.get("contKindCd").equals("20")) {
            contApndMap.put("contSplyTpCd", "MP4");
            contApndMap.put("vodQuntyHh", paramMap.get("vodCtQuntyHh"));
            contApndMap.put("vodQuntyMi", paramMap.get("vodCtQuntyMi"));
            contApndMap.put("vodQuntySec", paramMap.get("vodCtQuntySec"));
          } else {
            contApndMap.put("contSplyTpCd", uploadedData.get("orgnlExtn"));
            contApndMap.put("pageCnt", "0");
          }

//                contApndMap.put("lesnNm", "");
//                contApndMap.put("vodComnt", "");
          contApndMap.put("atacFileUid", uploadedData.get("atacFileUid"));
          contApndMap.put("atacDtlUid", uploadedData.get("atacDtlUid"));
          contApndMap.put("fstInsId", "ADMIN");
          contApndMap.put("lstUpdId", "ADMIN");
          contApndData.add(contApndMap);
        }
      }
    } else if ((paramMap.get("contKindCd").equals("10") || paramMap.get("contKindCd").equals("20"))
        && (paramMap.get("contRmthdCd").equals("20") || paramMap.get("contRmthdCd").equals("30"))) {

      Map contApndMap = new HashMap();

//            List<String> linkUrls = (List<String>) paramMap.get("linkUrls");
//            List<String> vodQuntyHhs = (List<String>) paramMap.get("vodQuntyHhs");
//            List<String> vodQuntyMis = (List<String>) paramMap.get("vodQuntyMis");
//            List<String> vodQuntySecs = (List<String>) paramMap.get("vodQuntySecs");

      contApndMap.put("contUid", paramMap.get("contUid"));
      contApndMap.put("contKindCd", paramMap.get("contKindCd"));
      contApndMap.put("seqNo", "1");
      contApndMap.put("contRmthdCd", paramMap.get("contRmthdCd"));
      contApndMap.put("linkUrl", paramMap.get("linkUrl"));
      contApndMap.put("nwinYn", "N");
//            contApndMap.put("contSplyTpCd", "");

      if (paramMap.get("contRmthdCd").equals("30")) {
        contApndMap.put("vodQuntyHh", paramMap.get("vodCtQuntyHh"));
        contApndMap.put("vodQuntyMi", paramMap.get("vodCtQuntyMi"));
        contApndMap.put("vodQuntySec", paramMap.get("vodCtQuntySec"));
      }
//                contApndMap.put("pageCnt", "0");
//                contApndMap.put("lesnNm", "");
//                contApndMap.put("vodComnt", "");
//                contApndMap.put("atacFileUid", "");
//                contApndMap.put("atacDtlUid", "");
      contApndMap.put("fstInsId", "ADMIN");
      contApndMap.put("lstUpdId", "ADMIN");
      contApndData.add(contApndMap);
    }

    if (uploadImage.isEmpty() || uploadImage == null) {
      if (paramMap.get("uploadImageUid").equals("")) {
        //  || paramMap.get("uploadImageUid").equals("")
        log.error("valid error!!");
        throw new Exception("강제 예외 발생!!!");
      } else {
        Map contApndMap = new HashMap();

        contApndMap.put("contUid", paramMap.get("contUid"));
        contApndMap.put("contKindCd", "50");
        contApndMap.put("seqNo", "1");
        contApndMap.put("contRmthdCd", "10");
//                    contApndMap.put("linkUrl", paramMap.get("linkUrl"));
        contApndMap.put("nwinYn", "N");
        contApndMap.put("contSplyTpCd", "PNG");
//                contApndMap.put("vodQuntyHh", "");
//                contApndMap.put("vodQuntyMi", "03");
//                contApndMap.put("vodQuntySec", "50");
//                contApndMap.put("pageCnt", "0");
//                contApndMap.put("lesnNm", "");
//                contApndMap.put("vodComnt", "");
        contApndMap.put("atacFileUid", atacFileUid);
        contApndMap.put("atacDtlUid", paramMap.get("uploadImageUid"));
        contApndMap.put("fstInsId", "ADMIN");
        contApndMap.put("lstUpdId", "ADMIN");
        contApndData.add(contApndMap);
      }
    } else {
      if (paramMap.get("uploadImageUid").equals("")) {
        Map uploadedData = null;//fileHandler.parseFileInfo(uploadImage,
            atacFileUid= paramMap.get("contKindCd").toString();

        log.info("uploaded data ===> ", uploadedData);

        // todo issue : commonMapper.getAtacDtlUid() 서비스단을 벋어나지 않으면 commit 이 안되어 동일번호 채번됨...
        uploadedData.put("atacDtlUid", 100);

        atacDtlData.add(uploadedData);

        Map contApndMap = new HashMap();

        contApndMap.put("contUid", paramMap.get("contUid"));
        contApndMap.put("contKindCd", "50");
        contApndMap.put("seqNo", "1");
        contApndMap.put("contRmthdCd", "10");
//                    contApndMap.put("linkUrl", paramMap.get("linkUrl"));
        contApndMap.put("nwinYn", "N");
        contApndMap.put("contSplyTpCd", uploadedData.get("orgnlExtn"));
//                contApndMap.put("vodQuntyHh", "");
//                contApndMap.put("vodQuntyMi", "03");
//                contApndMap.put("vodQuntySec", "50");
//                contApndMap.put("pageCnt", "0");
//                contApndMap.put("lesnNm", "");
//                contApndMap.put("vodComnt", "");
        contApndMap.put("atacFileUid", uploadedData.get("atacFileUid"));
        contApndMap.put("atacDtlUid", uploadedData.get("atacDtlUid"));
        contApndMap.put("fstInsId", "ADMIN");
        contApndMap.put("lstUpdId", "ADMIN");
        contApndData.add(contApndMap);
      }
    }

    if (paramMap.get("contKindCd").equals("30")) {
      List<Map> uploadedData = null;//fileHandler.parseFileInfo(uploadLessons,
          atacFileUid = paramMap.get("contKindCd").toString();

      log.info("uploaded data ===> ", uploadedData);

      int listCount = 0;

//            String[] uploadLessonsUidArr = paramMap.get("uploadLessonsUid").toString().split(",");
//
//            for (String lessonUid : uploadLessonsUidArr) {
//
//            }

      List<String> lesnNms = (List<String>) paramMap.get("lesnNms");
      List<String> vodQuntyHhs = (List<String>) paramMap.get("vodQuntyHhs");
      List<String> vodQuntyMis = (List<String>) paramMap.get("vodQuntyMis");
      List<String> vodQuntySecs = (List<String>) paramMap.get("vodQuntySecs");
      List<String> vodComnts = (List<String>) paramMap.get("vodComnts");

      for (Map uploadedItem : uploadedData) {
        listCount++;

        // todo issue : commonMapper.getAtacDtlUid() 서비스단을 벋어나지 않으면 commit 이 안되어 동일번호 채번됨...
        uploadedItem.put("atacDtlUid", listCount);

        atacDtlData.add(uploadedItem);

        Map contApndMap = new HashMap();

        contApndMap.put("contUid", paramMap.get("contUid"));
        contApndMap.put("contKindCd", paramMap.get("contKindCd"));
        contApndMap.put("seqNo", listCount);
        contApndMap.put("contRmthdCd", "10");
//                    contApndMap.put("linkUrl", paramMap.get("linkUrl"));
        contApndMap.put("nwinYn", "N");

        contApndMap.put("contSplyTpCd", "MP4");

        contApndMap.put("vodQuntyHh", vodQuntyHhs.get(listCount - 1));
        contApndMap.put("vodQuntyMi", vodQuntyMis.get(listCount - 1));
        contApndMap.put("vodQuntySec", vodQuntySecs.get(listCount - 1));
//                contApndMap.put("pageCnt", "0");

        contApndMap.put("lesnNm", lesnNms.get(listCount - 1));
        contApndMap.put("vodComnt", vodComnts.get(listCount - 1));
        contApndMap.put("atacFileUid", uploadedItem.get("atacFileUid"));
        contApndMap.put("atacDtlUid", uploadedItem.get("atacDtlUid"));
        contApndMap.put("fstInsId", "ADMIN");
        contApndMap.put("lstUpdId", "ADMIN");
        contApndData.add(contApndMap);
      }
    }

    if (atacDtlData.size() > 0) {
      paramMap.put("atacFileUid", atacFileUid);

      //commonMapper.insertAtacDtlInfo(atacDtlData);
    }

    if (paramMap.get("contUid").equals("")) {
      retVal = contentInfoMapper.insertContInfo(paramMap);
      contUid = paramMap.get("contUid").toString();
    } else {
      retVal = contentInfoMapper.updateContInfo(paramMap);
      contUid = paramMap.get("contUid").toString();
      contentInfoMapper.deleteContRlsTgtInfo(contUid);
      contentInfoMapper.deleteKwordInfo(contUid);
    }

    if (contApndData.size() > 0) {
      for (Map contApndItem : contApndData) {
        contApndItem.put("contUid", contUid);
      }
      contentInfoMapper.upsertContApndInfo(contApndData);
    }

    return convertMapToHtmlStringArray(paramMap, contUid);
  }

  private String convertMapToHtmlStringArray(Map<String, Object> paramMap, String contUid) {
    int retVal;

    List<String> rlsTgtCds = (List<String>) paramMap.get("rlsTgtCds");

    if (rlsTgtCds.size() > 0) {
      Map rlsTgtCdMap = new HashMap();
      rlsTgtCdMap.put("contUid", contUid);
      rlsTgtCdMap.put("rlsTgtCd", rlsTgtCds);

      retVal = contentInfoMapper.insertContRlsTgtInfo(rlsTgtCdMap);
    }

    if (!StringUtils.isEmptyOrWhitespace(paramMap.get("tagKwrd").toString())) {
      String[] tagKwrds = paramMap.get("tagKwrd").toString().split(",");

      Map tagKwrdMap = new HashMap();
      tagKwrdMap.put("contUid", contUid);
      tagKwrdMap.put("tagKwrd", tagKwrds);

      retVal = contentInfoMapper.insertKwordInfo(tagKwrdMap);
    }

    return contUid;
  }

  public Map<String, Object> selectShowByContentUpsertPop(String contUid) {
    return contentInfoMapper.selectShowByContentUpsertPop(contUid);
  }

  public List<Map<String, Object>> selectContApndAtacByContUid(String contUid) {
    return contentInfoMapper.selectContApndAtacByContUid(contUid);
  }

  public List<Map<String, Object>> selectContentTopicByTpicUid(Map<String, Object> paramMap) {
    return contentInfoMapper.selectContentTopicByTpicUid(paramMap);
  }
}
