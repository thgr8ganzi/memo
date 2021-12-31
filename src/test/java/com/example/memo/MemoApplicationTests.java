package com.example.memo;

import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class MemoApplicationTests {

    @Autowired
    MemoRepository memoRepository;

//    리포지토리 확인
    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

//    데이터 입력
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Memo memo = Memo.builder().memoText("sample..." + i).build();
            memoRepository.save(memo);
        });
    }

//    조회
    @Test
    public void testSelect2(){
//        존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("=================================================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

//    조회
    @Transactional
    @Test
    public void testSelect(){

        Long mno = 50L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("===================================================");
        System.out.println(memo);
    }

//    수정
    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

//    삭제
    @Test
    public void testDelete(){
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

//    페이징처리(10개씩 묶기)
    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("=========================================");
        System.out.println("Total Page" + result.getTotalPages());// 몇페이지
        System.out.println("Total Count" + result.getTotalElements());// 총개수
        System.out.println("Page Num" + result.getNumber());//현재 페이지 번호
        System.out.println("Page Size" + result.getSize());//페이지당 데이터 개수
        System.out.println("has next page" + result.hasNext());//다음 페이지 존재 여부
        System.out.println("first Page?" + result.isFirst());//시작 페이지 여부
        System.out.println("==============================================");
        for (Memo memo: result.getContent()){
            System.out.println(memo);
        }
    }
//    페이징처리 역순
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

//    쿼리메소드
    @Test
    public void testQuertyMethod(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for (Memo memo : list){
            System.out.println(memo);
        }
    }
//    쿼리 메소드 페이징처리
    @Test
    public void testQueryMethodWithPage(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }
//    쿼리메소드 페이징처리 삭제
    @Commit
    @Test
    @Transactional
    public void testDelteQueryMethod(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void getListDesc(){
        memoRepository.getListDesc();
    }

    @Test
    public void updateMemoText(){
        Long mno = 99L;
        String memoText = "updateSample....99";
        memoRepository.updateMemoText(mno, memoText);
    }

    @Test
    public void updateMemoText2(){
        Memo memo = Memo.builder().mno(10L).memoText("updateSample....10").build();
        memoRepository.updateMemoText2(memo);
    }

    @Test
    public void getListWithQuery(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.getListWithQuery(10L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    public void getListWithQueryObject(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object[]> result = memoRepository.getListWithQueryObject(10L, pageable);
        result.get().forEach(memo -> System.out.println(memo.toString()));
    }

    @Test
    public void getNativeResult(){
        memoRepository.getNativeResult();
    }
}





