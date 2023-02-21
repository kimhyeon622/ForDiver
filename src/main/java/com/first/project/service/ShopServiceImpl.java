package com.first.project.service;

import com.first.project.dao.ShopDAO;
import com.first.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private ModelAndView mav;

    private final HttpSession session;

    private final ShopDAO sdao;

    @Override
    public List<List<Object>> buttonList(DivEqSortDTO divEqSort) {

        List<List<Object>> btnBigList = new ArrayList<>();
        List<DivEqSortDTO> btnTotList = sdao.btnList(divEqSort);
        List<Object> btnList= new ArrayList<>();


        for(int i =0; i < btnTotList.size(); i++){

            if(btnTotList.get(i).getDivEqSoNum().length() > divEqSort.getDivEqSoNum().length() && btnTotList.get(i).getDivEqSoNum().indexOf('/', divEqSort.getDivEqSoNum().length()) != -1){
                if(btnTotList.get(i).getDivEqSoNum().indexOf('/', divEqSort.getDivEqSoNum().length()) == (btnTotList.get(i).getDivEqSoNum().length()-1)){
                    btnList.add(btnTotList.get(i));
                }
            }
        }
        btnBigList.add(btnList);
        // 하위 버튼 목록 추가
        

        // History에 추가할 정보 리스트
        List<Object> hisList = new ArrayList<>();
        DivEqSortDTO hisDTO = null;

        if(divEqSort.getDivEqSoNum().equals("")){   // 홈 버튼 누를경우

        }else{
            hisList.add(divEqSort);   // List<DTO>에 Num, Name 추가 (0번째)

            int hisIndex1 = divEqSort.getDivEqSoNum().substring(0,divEqSort.getDivEqSoNum().length()-1).lastIndexOf('/');
            if(hisIndex1 != -1){
                String hisNum1 = divEqSort.getDivEqSoNum().substring(0, hisIndex1+1); // A/1/ 가져옴
                hisDTO = sdao.hisList(hisNum1);    // sdao 가서 Name 가져오기
                hisList.add(hisDTO);    // List<DTO>에 Num, Name 추가 (1번째)

                System.out.println(hisDTO);

                int hisIndex2 = hisNum1.substring(0,hisNum1.length()-1).lastIndexOf('/');
                if(hisIndex2 != -1){
                    String hisNum2 = hisNum1.substring(0, hisIndex2+1);  // A/ 가져옴
                    hisDTO = sdao.hisList(hisNum2); // sdao 가서 Name 가져오기
                    hisList.add(hisDTO);    // List<DTO>에 Num, Name 추가 (2번째)

                    System.out.println(hisDTO);
                    int hisIndex3 = hisNum2.substring(0,hisNum2.length()-1).lastIndexOf('/');  // -1 출력, 끝

                }
            }
        }
        btnBigList.add(hisList);
        // 버튼 히스토리 추가


        List<Object> eqList = equipList(1, divEqSort.getDivEqSoNum());
        btnBigList.add(eqList);


        return btnBigList;
    }

    @Override
    public ModelAndView shopList(int page) {

        PageDTO paging = new PageDTO();
        paging.setPage(page);
        mav = new ModelAndView();

        int block = 5;
        // 한 화면에 보여줄 페이지 번호 개수

        paging.setLimit(8);
        // 한 화면에 보여줄 게시글 개수
        int limit = paging.getLimit();

        // 전체 게시글 개수
        int eqCount = sdao.eqCount("");

        // ceil : (소숫점)올림
        int maxPage = (int) (Math.ceil((double) eqCount / limit));
        int startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        if (paging.getPage() > maxPage) {
            paging.setPage(maxPage);
        }

        int startRow = (paging.getPage() - 1) * limit + 1;
        int endRow = paging.getPage() * limit;

        PageDTO paging1 = new PageDTO();

        paging1.setPage(paging.getPage());
        paging1.setLimit(limit);
        paging1.setStartRow(startRow);
        paging1.setEndRow(endRow);
        paging1.setStartPage(startPage);
        paging1.setEndPage(endPage);
        paging1.setMaxPage(maxPage);
        mav.addObject("paging", paging1);

        DivEqListDTO divEqList = new DivEqListDTO();
        divEqList.setDivEqSortNum("");
        divEqList.setStartRow(paging1.getStartRow());
        divEqList.setEndRow(paging1.getEndRow());

        List<DivEqDTO> eqList = sdao.eqList(divEqList);
        for(int i = 0; i < eqList.size(); i++){
            StringBuffer eqPrice = new StringBuffer();
            if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 1 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 3){
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqList.get(i).getDivEqPrice()));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 4 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 6){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 7 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 9){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-6,",");
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 10 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 12){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-9,",");
                eqPrice.insert(eqPrice.length()-6,",");
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else{
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqList.get(i).getDivEqPrice()));
            }
        }
        String loginId = null;
        try{
            loginId = session.getAttribute("loginId").toString();
        }catch (Exception e){
        }

        if(loginId != null){
            String dbBool = sdao.eqBasketView(loginId);
            if(dbBool == null){
                sdao.shopMemSet(loginId);
            }
        }

        mav.addObject("eqList", eqList);

        mav.setViewName("shopList");

        return mav;
    }

    @Override
    public List<Object> equipList(int page, String divEqSoNum) {

        List<Object> TotList = new ArrayList<>();

        PageDTO paging = new PageDTO();
        paging.setPage(page);

        int block = 5;
        // 한 화면에 보여줄 페이지 번호 개수

        paging.setLimit(8);
        // 한 화면에 보여줄 게시글 개수
        int limit = paging.getLimit();

        // 전체 게시글 개수
        int eqCount = sdao.eqCount(divEqSoNum);

        // ceil : (소숫점)올림
        int maxPage = (int) (Math.ceil((double) eqCount / limit));
        int startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        if (paging.getPage() >= maxPage) {
            paging.setPage(maxPage);
            startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        }

        int startRow = (paging.getPage() - 1) * limit + 1;
        int endRow = paging.getPage() * limit;

        PageDTO paging1 = new PageDTO();

        paging1.setPage(paging.getPage());
        paging1.setLimit(limit);
        paging1.setStartRow(startRow);
        paging1.setEndRow(endRow);
        paging1.setStartPage(startPage);
        paging1.setEndPage(endPage);
        paging1.setMaxPage(maxPage);

        DivEqListDTO divEqList = new DivEqListDTO();
        divEqList.setDivEqSortNum(divEqSoNum);
        divEqList.setStartRow(paging1.getStartRow());
        divEqList.setEndRow(paging1.getEndRow());

        List<DivEqDTO> eqList = sdao.eqList(divEqList);
        for(int i = 0; i < eqList.size(); i++){
            StringBuffer eqPrice = new StringBuffer();
            if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 1 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 3){
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqList.get(i).getDivEqPrice()));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 4 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 6){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 7 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 9){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-6,",");
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else if(String.valueOf(eqList.get(i).getDivEqPrice()).length() >= 10 && String.valueOf(eqList.get(i).getDivEqPrice()).length() <= 12){
                eqPrice.append(eqList.get(i).getDivEqPrice());
                eqPrice.insert(eqPrice.length()-9,",");
                eqPrice.insert(eqPrice.length()-6,",");
                eqPrice.insert(eqPrice.length()-3,",");
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqPrice));
            }else{
                eqList.get(i).setDivEqPriceStr(String.valueOf(eqList.get(i).getDivEqPrice()));
            }
        }


        TotList.add(eqList);
        TotList.add(paging1);

        return TotList;
    }

    @Override
    public DivEqDTO eqDetail(String divEqNum) {

        DivEqDTO eqDetail = sdao.eqDetail(divEqNum);
        StringBuffer eqPrice = new StringBuffer();
        if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 1 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 3){
            eqDetail.setDivEqPriceStr(String.valueOf(eqDetail.getDivEqPrice()));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 4 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 6){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 7 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 9){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 10 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 12){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-9,",");
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else{
            eqDetail.setDivEqPriceStr(String.valueOf(eqDetail.getDivEqPrice()));
        }

        return eqDetail;
    }

    public DivEqDTO eqDetail(String divEqNum, int eqCount) {

        DivEqDTO eqDetail = sdao.eqDetail(divEqNum);
        eqDetail.setDivEqPrice(eqDetail.getDivEqPrice());
        StringBuffer eqPrice = new StringBuffer();
        if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 1 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 3){
            eqDetail.setDivEqPriceStr(String.valueOf(eqDetail.getDivEqPrice()));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 4 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 6){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 7 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 9){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getDivEqPrice()).length() >= 10 && String.valueOf(eqDetail.getDivEqPrice()).length() <= 12){
            eqPrice.append(eqDetail.getDivEqPrice());
            eqPrice.insert(eqPrice.length()-9,",");
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setDivEqPriceStr(String.valueOf(eqPrice));
        }else{
            eqDetail.setDivEqPriceStr(String.valueOf(eqDetail.getDivEqPrice()));
        }

        eqDetail.setTotalPriceInt(eqDetail.getDivEqPrice()*eqCount);
        eqPrice = new StringBuffer();
        if(String.valueOf(eqDetail.getTotalPriceInt()).length() >= 1 && String.valueOf(eqDetail.getTotalPriceInt()).length() <= 3){
            eqDetail.setTotalPriceStr(String.valueOf(eqDetail.getTotalPriceInt()));
        }else if(String.valueOf(eqDetail.getTotalPriceInt()).length() >= 4 && String.valueOf(eqDetail.getTotalPriceInt()).length() <= 6){
            eqPrice.append(eqDetail.getTotalPriceInt());
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setTotalPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getTotalPriceInt()).length() >= 7 && String.valueOf(eqDetail.getTotalPriceInt()).length() <= 9){
            eqPrice.append(eqDetail.getTotalPriceInt());
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setTotalPriceStr(String.valueOf(eqPrice));
        }else if(String.valueOf(eqDetail.getTotalPriceInt()).length() >= 10 && String.valueOf(eqDetail.getTotalPriceInt()).length() <= 12){
            eqPrice.append(eqDetail.getTotalPriceInt());
            eqPrice.insert(eqPrice.length()-9,",");
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            eqDetail.setTotalPriceStr(String.valueOf(eqPrice));
        }else{
            eqDetail.setTotalPriceStr(String.valueOf(eqDetail.getTotalPriceInt()));
        }


        return eqDetail;
    }

    @Override
    public String customReset(String loginId) {
        ShopMemDTO shopMem = new ShopMemDTO();
        shopMem.setLoginId(loginId);
        shopMem.setBtCustomList("");
        sdao.cusDBSet(shopMem);
        return "OK";
    }

    @Override
    public ModelAndView eqPay(List<String> eqList, String loginId) {
        System.out.println("eqList : " + eqList.size());


        for(int i =0; i < eqList.size(); i=i+2){
            DivEqDTO divEq = new DivEqDTO();

            divEq.setDivEqNum(eqList.get(i).toString());
            System.out.println("DivEqNum : " + divEq.getDivEqNum());
            divEq.setEqCount(Integer.parseInt(eqList.get(i+1).toString()));
            System.out.println("EqCount : " + divEq.getEqCount());
            sdao.eqPay(divEq);
        }

        ShopMemDTO shopMem = new ShopMemDTO();
        shopMem.setLoginId(loginId);
        shopMem.setEqBasketList(" ");
        sdao.eqBasketReset(shopMem);

        return mav;
    }

    @Override
    public List<Object> buttonCustom(int page, int cusCurPage, String cbxList, String loginId) {

        // 이후 페이지 체크박스 목록
        List<String> futCbxList = new ArrayList<>();

        // 체크박스 목록 정리
        int cbxIndex = -1;
        String input = "";
        for(int i = 0; i < cbxList.length(); i++){
            char cbxChar = cbxList.charAt(i);
            if(cbxChar == '/'){
                input += cusCurPage + "/" + cbxList.substring(cbxIndex+1, i) + " ";
                cbxIndex = i;
            }
        }

        // DB가서 회원에 해당하는 cbx목록 가져옴
        String DBcbxList = sdao.cusDBView(loginId);
        if(DBcbxList == null){
            DBcbxList = "";
        }

        // 이전 페이지의 DB내 체크리스트 전부 제거 (아래에서 다시 재정립 후 추가)
        int dbIndex1 = DBcbxList.length() -1;  // '/'
        int dbIndex2 = DBcbxList.length();  // ' '

        if(DBcbxList != ""){
            for(int i = DBcbxList.length()-1; i >= 0 ; i--){
                char dbChar = DBcbxList.charAt(i);
                if(dbChar == '/'){
                    dbIndex1 = i;
                }
                if(dbChar == ' '){
                    if(i < DBcbxList.length()-1){
                        if(DBcbxList.substring(i+1, dbIndex1).equals(String.valueOf(cusCurPage))){
                            DBcbxList = DBcbxList.replaceFirst(DBcbxList.substring(i+1,dbIndex2+1), "");
                        }

                    }else{

                    }
                    dbIndex2 = i;
                }

                if(i == 0){
                    if(DBcbxList.substring(i, dbIndex1).equals(String.valueOf(cusCurPage))){
                        DBcbxList = DBcbxList.replaceFirst(DBcbxList.substring(i,dbIndex2+1), "");
                    }
                }
            }
        }
        // 이전 페이지의 체크리스트를 새로 DB에 저장
        DBcbxList += input;
        // 이상태로 DB로 저장
        ShopMemDTO shopMem = new ShopMemDTO();
        shopMem.setLoginId(loginId);
        shopMem.setBtCustomList(DBcbxList);
        sdao.cusDBSet(shopMem);


        int cusTotal = 0;

        if(DBcbxList != ""){
        // 오픈할 페이지의 체크리스트 확인
        // 인덱스 초기화
        dbIndex1 = 1;   // '/'
        dbIndex2 = -1;  // ' '

        for(int i = 0; i < DBcbxList.length(); i++){
            char dbChar = DBcbxList.charAt(i);
            if(dbChar == '/'){
                dbIndex1 = i;
            }
            if(dbChar == ' '){
                cusTotal++; // 총 갯수 파악
                if(DBcbxList.substring(dbIndex2+1, dbIndex1).equals(String.valueOf(page))){
                    futCbxList.add(DBcbxList.substring(dbIndex1+1,i));
                }
                dbIndex2 = i;
            }
        }
        }

        // 총 리스트
        List<Object> cusBigList = new ArrayList<>();

        // 모든 버튼리스트 불러오기
        DivEqSortDTO divEqSort = new DivEqSortDTO();
        divEqSort.setDivEqSoNum("");
        List<DivEqSortDTO> btnTotList = sdao.btnList(divEqSort);

        // 총 버튼 리스트 페이징 처리
        PageDTO paging = new PageDTO();
        paging.setPage(page);

        int block = 5;
        // 한 화면에 보여줄 페이지 번호 개수

        paging.setLimit(18);
        // 한 화면에 보여줄 버튼 개수
        int limit = paging.getLimit();

        // 전체 버튼 개수
        int btnCount = btnTotList.size();

        // ceil : (소숫점)올림
        int maxPage = (int) (Math.ceil((double) btnCount / limit));
        int startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }
        if (paging.getPage() >= maxPage) {
            paging.setPage(maxPage);
            startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        }
        int startRow = (paging.getPage() - 1) * limit + 1;
        int endRow = paging.getPage() * limit;

        PageDTO paging1 = new PageDTO();

        paging1.setPage(paging.getPage());
        paging1.setLimit(limit);
        paging1.setStartRow(startRow);
        paging1.setEndRow(endRow);
        paging1.setStartPage(startPage);
        paging1.setEndPage(endPage);
        paging1.setMaxPage(maxPage);

        // 페이징 처리한 버튼 목록 (해당 페이지의 버튼목록 18개 가져옴)
        List<DivEqSortDTO> btnList = sdao.cusBtnList(paging1);


        // 페이징 처리한 버튼목록에서 체크된 목록들 배열에 추가등록
        if(futCbxList != null){
            for(int i = 0; i < futCbxList.size(); i++){
                btnList.get(Integer.parseInt(futCbxList.get(i))-1).setCbxBool("checked");
            }
        }
        System.out.println("btnList" + btnList);
        System.out.println("futCbxList" + futCbxList);


        // 체크처리완료 된 페이징 버튼목록 추가, 페이징처리된 페이징 추가
        cusBigList.add(btnList);
        cusBigList.add(paging1);
        cusBigList.add(cusTotal);


        return cusBigList;
    }

    @Override
    public List<Object> cusBtnList(String loginId) {

        List<Object> btnBigList = new ArrayList<>();

        String DBcbxList = sdao.cusDBView(loginId);
        List<DivEqSortDTO> btnTotList = new ArrayList<>();
        List<Integer> rowList = new ArrayList<>();
        if(DBcbxList == null){
            DBcbxList="";
        }
        int dbIndex1 = 1;
        int dbIndex2 = -1;
        if(DBcbxList != "") {
            // 오픈할 페이지의 체크리스트 확인
            for (int i = 0; i < DBcbxList.length(); i++) {
                char dbChar = DBcbxList.charAt(i);
                if (dbChar == '/') {
                    dbIndex1 = i;
                }
                if (dbChar == ' ') {

                    int rowNum = ((Integer.parseInt(DBcbxList.substring(dbIndex2 + 1, dbIndex1))-1)*18) + Integer.parseInt(DBcbxList.substring(dbIndex1 + 1, i));
                    rowList.add(rowNum);

                    dbIndex2 = i;
                }
            }

            for(int i = 0; i < rowList.size(); i++){
                btnTotList.add(sdao.cusSel(rowList.get(i)));
            }
            System.out.println(btnTotList);
        btnBigList.add(btnTotList);
        // 하위 버튼 목록 추가
        }

        return btnBigList;
    }

    @Override
    public List<Object> eqBasket(String divEqNum,int eqCount, String loginId) {

        List<Object> basketList = new ArrayList<>();

        // 해당 장비 정보 다 가져옴
        DivEqDTO eqDetail = sdao.eqDetail(divEqNum);

        System.out.println(eqDetail);

        // 수량체크
        if(eqDetail.getDivHave() < eqCount){
            basketList.add("none");
            return basketList;
        }

        // 회원 장바구니 DB에 스트링으로 추가 ("장비번호/구매수량 ")
        ShopMemDTO shopMem = new ShopMemDTO();
        shopMem.setLoginId(loginId);
        shopMem.setEqBasketList(divEqNum+"/"+eqCount+" ");
        sdao.eqBasketSet(shopMem);


        String eqStr = "_mask. A/2/1/ A/2/2/ A/2/4/ A/2/5/ A/1/1/ A/5/1/ A/5/2/ A/5/3/ _snok. A/3/1/ A/3/2/ A/3/3/ A/1/2/ A/5/1/ A/5/2/ A/5/3/ _flip. A/4/1/ A/4/2/ A/1/3/ A/5/2/ A/5/3/ _suit. A/10/1/ A/10/2/ A/10/3/ A/10/4/ A/10/5/ A/11/1/ A/11/2/ A/11/3/ A/11/4/ A/5/3/ _glob. A/7/1/ A/7/2/ A/7/3/ _boots. A/8/1/ A/8/2/ A/8/3/ A/8/4/ _regu. B/1/1/ B/1/2/ B/1/3/ B/1/4/ B/1/5/ B/10/1/ B/10/3/ B/10/2/ _octo. B/2/1/ B/2/2/ B/2/3/ B/10/1/ B/10/3/ B/10/2/ _bcd. B/4/1/ B/4/2/ B/4/3/ B/10/2/ _gaug. B/3/1/ B/3/2/ B/3/3/ B/3/4/ B/10/1/ B/10/3/ B/10/2/ _comp. A/1/5/ B/5/1/ B/5/2/ B/5/3/ _airp. B/8/1/ ";

        // 아이템 목록에 있을경우 추가
        int eqBool = eqStr.lastIndexOf(eqDetail.getDivEqSortNum());

        if(eqBool==-1){
            basketList.add("no");
            return basketList;
        }

        List<Object> eqName = new ArrayList<>();
        List<Object> eqPhoto = new ArrayList<>();

        while (eqBool != -1){
            int eqIndex1 = 0;
            int eqIndex2 = 0;
            System.out.println("eqBool" + eqBool);

            if(eqBool != -1){
                eqIndex1 = eqStr.lastIndexOf('_',eqBool);
                eqIndex2 = eqStr.lastIndexOf('.',eqBool);

                eqName.add(eqStr.substring(eqIndex1+1,eqIndex2));
                eqPhoto.add(eqDetail.getDivPhotoName());
            }

            eqBool = eqStr.lastIndexOf(eqDetail.getDivEqSortNum(), eqBool-1);
        }
        basketList.add(eqName);
        basketList.add(eqPhoto);

//        String[] mask = {"A/2/1/","A/2/2/","A/2/4/","A/2/5/","A/1/1/","A/5/1/","A/5/2/","A/5/3/"};
//        String[] snok = {"A/3/1/","A/3/2/","A/3/3/","A/1/2/","A/5/1/","A/5/2/","A/5/3/"};
//        String[] flip = {"A/4/1/","A/4/2/","A/1/3/","A/5/2/","A/5/3/"};
//        String[] suit = {"A/10/1/","A/10/2/","A/10/3/","A/10/4/","A/10/5/","A/11/1/","A/11/2/","A/11/3/","A/11/4/","A/5/3/"};
//        String[] glob = {"A/7/"};
//        String[] boots = {"A/8/"};
//
//        String[] regu = {"B/1/1/","B/1/2/","B/1/3/","B/1/4/","B/1/5/","B/10/1/","B/10/2/","B/10/3/"};
//        String[] octo = {"B/2/","B/10/1/","B/10/2/","B/10/3/"};
//        String[] bcd = {"B/4/1/","B/4/2/","B/4/3/","B/10/2/"};
//        String[] gaug = {"B/3/1/","B/3/2/","B/3/3/","B/3/4/","B/10/1/","B/10/2/","B/10/3/"};
//        String[] comp = {"A/1/5/","B/5/1/","B/5/2/","B/5/3/"};
//        String[] airp = {"B/8/1/","B/8/2/","B/8/3/"};

        return basketList;
    }

    @Override
    public ModelAndView shopCheckout(String loginId) {

        ModelAndView mav = new ModelAndView();

        // memId에 있는 장바구니 가져옴 (원래는 아이디에 해당하는 값을 찾아 가져온다)
        String eqBasketList = sdao.eqBasketView(loginId);
        // 장바구니 첫 공백 무조건 넣어야함! (회원 데이터 넣을 때)

        System.out.println("eqBasketList : " + eqBasketList + " 길이 : " + eqBasketList.length());

        List<DivEqDTO> divEqList = new ArrayList<>();

        int totalPriceInt = 0;

        int voidIndex = eqBasketList.length()-1;
        int slashIndex = eqBasketList.length()-1;

        char bkChar = '0';
        String eqNum = null;
        int eqCount = 0;
        StringBuffer eqBuffer = new StringBuffer(eqBasketList);

        System.out.println("eqBuffer : " + eqBuffer + " 길이 : " + eqBuffer.length());

        int a = 0;
        int b = 0;
        int c = 0;
        String d = "";

        for(int i = eqBasketList.length()-1 ; i >= 0; i--){

            bkChar = eqBuffer.charAt(i);
            if(bkChar == '/'){
                slashIndex = i;
            }
            if(bkChar == ' ' || i == 0){
                if(i < eqBasketList.length()-1){
                    eqNum = eqBuffer.substring(i+1, slashIndex);
                    eqCount = Integer.parseInt(eqBuffer.substring(slashIndex+1, voidIndex));

                    if(i != 0){
                        if(eqBuffer.lastIndexOf(" "+eqNum+ "/",i-1) == -1){
                            DivEqDTO divEq = eqDetail(eqNum,eqCount);
                            divEq.setEqCount(eqCount);
                            divEqList.add(divEq);
                            totalPriceInt += divEq.getTotalPriceInt();
                        }else{
                            a = eqBuffer.lastIndexOf(" "+eqNum+ "/",i-1);  // 앞 공백 포함 위치
                            b = eqBuffer.indexOf("/",a+1);    // 슬래쉬 위치
                            c = eqBuffer.indexOf(" ",a+1);    // 다음 공백 위치

                            d = String.valueOf(Integer.parseInt(eqBuffer.substring(b+1,c)) + eqCount);  // 총 수량
                            eqBuffer = eqBuffer.replace(b+1,c, d);

                        }
                    }else{
                        DivEqDTO divEq = eqDetail(eqNum,eqCount);
                        divEq.setEqCount(eqCount);
                        divEqList.add(divEq);
                        totalPriceInt += divEq.getTotalPriceInt();
                    }
                }
                System.out.println(eqBuffer);
                voidIndex = eqBuffer.indexOf(" ",i);
            }
        }

        System.out.println("buyList : " + divEqList);

        String totalPriceStr = "";
        StringBuffer eqPrice = new StringBuffer();
        if(String.valueOf(totalPriceInt).length() >= 1 && String.valueOf(totalPriceInt).length() <= 3){
            totalPriceStr = (String.valueOf(totalPriceInt));
        }else if(String.valueOf(totalPriceInt).length() >= 4 && String.valueOf(totalPriceInt).length() <= 6){
            eqPrice.append(totalPriceInt);
            eqPrice.insert(eqPrice.length()-3,",");
            totalPriceStr = (String.valueOf(eqPrice));
        }else if(String.valueOf(totalPriceInt).length() >= 7 && String.valueOf(totalPriceInt).length() <= 9){
            eqPrice.append(totalPriceInt);
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            totalPriceStr = (String.valueOf(eqPrice));
        }else if(String.valueOf(totalPriceInt).length() >= 10 && String.valueOf(totalPriceInt).length() <= 12){
            eqPrice.append(totalPriceInt);
            eqPrice.insert(eqPrice.length()-9,",");
            eqPrice.insert(eqPrice.length()-6,",");
            eqPrice.insert(eqPrice.length()-3,",");
            totalPriceStr = (String.valueOf(totalPriceInt));
        }else{
            totalPriceStr = (String.valueOf(eqPrice));
        }

        String totalPriceShipStr = "";
        int totalPriceShipInt = totalPriceInt +2000;
        StringBuffer eqPrice2 = new StringBuffer();
        if(String.valueOf(totalPriceShipInt).length() >= 1 && String.valueOf(totalPriceShipInt).length() <= 3){
            totalPriceShipStr = (String.valueOf(totalPriceShipInt));
        }else if(String.valueOf(totalPriceShipInt).length() >= 4 && String.valueOf(totalPriceShipInt).length() <= 6){
            eqPrice2.append(totalPriceShipInt);
            eqPrice2.insert(eqPrice2.length()-3,",");
            totalPriceShipStr = (String.valueOf(eqPrice2));
        }else if(String.valueOf(totalPriceShipInt).length() >= 7 && String.valueOf(totalPriceShipInt).length() <= 9){
            eqPrice2.append(totalPriceShipInt);
            eqPrice2.insert(eqPrice2.length()-6,",");
            eqPrice2.insert(eqPrice2.length()-3,",");
            totalPriceShipStr = (String.valueOf(eqPrice2));
        }else if(String.valueOf(totalPriceShipInt).length() >= 10 && String.valueOf(totalPriceShipInt).length() <= 12){
            eqPrice2.append(totalPriceShipInt);
            eqPrice2.insert(eqPrice2.length()-9,",");
            eqPrice2.insert(eqPrice2.length()-6,",");
            eqPrice2.insert(eqPrice2.length()-3,",");
            totalPriceShipStr = (String.valueOf(totalPriceShipInt));
        }else{
            totalPriceShipStr = (String.valueOf(eqPrice2));
        }

        mav.addObject("totalEq",totalPriceStr );
        mav.addObject("totalPr", totalPriceShipStr);

        mav.addObject("buyList", divEqList);

        mav.setViewName("shopCheckout");

        return mav;
    }

    @Override
    public ModelAndView eqBuyList(String loginId, DivEqDTO eqBuyList) {

        ModelAndView mav = new ModelAndView();

        // DTO중에 장비번호를 divEqNum, 선택 개수를 totalPriceStr 로 가져온다.
        System.out.println(eqBuyList.getDivEqNum());
        System.out.println(eqBuyList.getTotalPriceStr());

        ShopMemDTO shopMem = new ShopMemDTO();
        shopMem.setLoginId(loginId);

        String buyList = " ";

        if(eqBuyList.getDivEqNum() == null){
            shopMem.setEqBasketList(" ");
            sdao.buyListSet(shopMem);
            mav.setViewName("shopCheckout");
            return mav;
        }
        if(eqBuyList.getTotalPriceStr() == null) {
            shopMem.setEqBasketList(" ");
            sdao.buyListSet(shopMem);
            mav.setViewName("shopCheckout");
            return mav;
        }
        int eqNumIndex1 = eqBuyList.getDivEqNum().lastIndexOf(",",eqBuyList.getDivEqNum().length());
        int eqNumIndex2 = eqBuyList.getDivEqNum().length();
        int eqCountIndex1 = eqBuyList.getTotalPriceStr().lastIndexOf(",",eqBuyList.getTotalPriceStr().length());
        int eqCountIndex2 = eqBuyList.getTotalPriceStr().length();

        while(eqNumIndex1 != -1){

            buyList += eqBuyList.getDivEqNum().substring(eqNumIndex1+1,eqNumIndex2)
                    + "/" + eqBuyList.getTotalPriceStr().substring(eqCountIndex1+1,eqCountIndex2) +" ";

            eqNumIndex2 = eqBuyList.getDivEqNum().indexOf(",",eqNumIndex1);
            eqNumIndex1 = eqBuyList.getDivEqNum().lastIndexOf(",",eqNumIndex1-1);

            eqCountIndex2 = eqBuyList.getTotalPriceStr().indexOf(",",eqCountIndex1);
            eqCountIndex1 = eqBuyList.getTotalPriceStr().lastIndexOf(",",eqCountIndex1-1);
        }

        buyList += eqBuyList.getDivEqNum().substring(0, eqNumIndex2)
                + "/" + eqBuyList.getTotalPriceStr().substring(0, eqCountIndex2) +" ";


        // 원래는 memId 넣고 특정 열을 변경해야함
        shopMem.setEqBasketList(buyList);
        sdao.buyListSet(shopMem);

        return shopCheckout(loginId);
    }





}
