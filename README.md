# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# merge 이후 미션 수행 명령어
```
git checkout songteaheon  
git fetch upstream songteaheon  
git rebase upstream/songteaheon
```  


# TODO
- [x] 지하철역 인수 테스트를 완성하세요.
  - [x] 지하철역 목록 조회 인수 테스트 작성하기
  - [x] 지하철역 삭제 인수 테스트 작성하기

- [x] 지하철 노선 생성
- [x] 지하철 노선 목록 조회
- [x] 지하철 노선 조회
- [x] 지하철 노선 수정
- [x] 지하철 노선 삭제

- [x] 요구사항을 정의한 인수 조건을 도출하세요.
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
- [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.
- [x] 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리
- [x] 예외 케이스에 대한 검증도 포함하세요.
- [x] 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링
- [x] 요구사항 설명에서 제공되는 요구사항을 기반으로 지하철 구간 관리 기능을 구현하세요.

- 구간 추가 인수조건 도출
- [x] 지하철 노선에 구간을 등록하는 기능의 인수조건 도출
- [x] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
- [x] 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
- [x] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.

- 구간 추가 기능 구현
- [X] 지하철 노선에 구간을 등록하는 기능을 구현
- [X] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
- [X] 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
- [X] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.


- 구간 삭제 기능 인수조건 도출
- [X] 지하철 노선에 구간을 제거하는 기능의 인수조건 도출
- [X] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
- [X] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
- [X] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

- 구간 삭제 기능 구현
- [X] 지하철 노선에 구간을 제거하는 기능 구현
- [X] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
- [X] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
- [X] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

- 리뷰 반영
- [X] SectionController 메서드 네임 수정
- [ ] SectionController와 LineController 합치기
- [ ] JPA를 사용하다보니 @NoArgsContructor 대신, 가시성을 protected로 선언하기
- [ ] Section은 line과 라이프사이클을 함께 하는데, 고아 객체에 대한 처리를 고민해보면 좋을 것 같아요.
- [ ] line.getStations() 메서드의 Stations를 불변객체로 변경하기.
- [ ] 이 방법으로 섹션이 추가되면, line에서 갖고 있던 섹션은 사라지지 않을까요?  -> 질문하기
- [ ] id 비교하는 부분 객체 비교로 변경하기
- [ ] save인데 클래스 선언부에 정의된 ReadOnly=true 지우기
- [ ] line에 section이 종속되기 때문. sectionService의 save를 따로 호출하지 않도록 변경하기
- [ ] line에 section이 종속되기 때문. section을 따로 조회하지 않도록 수정. 또 sectionService의 delete를 따로 호출하지 않도록 변경
- [ ] @nested사용해서 테스트 코드 리팩토링 
- [ ] 이렇게 assertThat을 여러 개 써야 한다면, assertAll을 사용하는 걸로 변경하기.
- [ ] 생성된_LINE_조회(lineResponse); 코드 확인하기
- [ ] 이름으로 비교하기 보다는, JPA Entity니 생성한 객체를 담고있는지로 비교하기. 그리고 이런 경우는 ContainsAll를 사용하기
- [ ] assertion hasSize로 사용하기
- [ ] 
