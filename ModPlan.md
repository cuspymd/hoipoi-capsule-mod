# 호이포이 캡슐 모드 (HoiPoi Capsule Mod) 기획서

## 📋 프로젝트 개요

**모드명**: HoiPoi Capsule Mod  
**개발 플랫폼**: Fabric
**타겟 마인크래프트 버전**: 1.21.4
**개발 언어**: Java  
**개발자**: 초보자 대상 단계별 구현

## 🎯 컨셉 및 목표

드래곤볼의 호이포이 캡슐에서 영감을 받아, 건물이나 구조물을 작은 캡슐에 저장하고 언제든지 다시 배치할 수 있는 시스템을 구현합니다.

**핵심 가치:**
- 편의성: 거대한 건축물을 간편하게 이동
- 창의성: 미리 제작된 구조물로 빠른 건축
- 탐험: 모험 중에도 집을 가지고 다니기
- 협업: 구조물 공유를 통한 커뮤니티 활성화

## 🚀 단계별 개발 로드맵

### Phase 1: 기본 캡슐 시스템 (2-3주)
**목표**: 간단한 3x3x3 영역 저장/배치 기능

**구현 기능:**
- 빈 캡슐 아이템 제작
- 우클릭으로 3x3x3 영역 저장
- 저장된 구조물을 다른 곳에 배치
- 기본적인 NBT 데이터 저장

**핵심 클래스:**
```java
- BasicCapsule.java (아이템)
- CapsuleData.java (구조물 데이터)
- CapsuleUtils.java (저장/배치 로직)
```

### Phase 2: 홀드 기반 영역 선택 시스템 (2-3주)
**목표**: 캡슐만으로 홀드 기반 실시간 영역 선택

**구현 기능:**
- 우클릭 홀드로 실시간 영역 표시
- 시선 방향 기반 영역 위치 조정
- 마우스 휠로 거리 조정 (3-10블록)
- 실시간 미리보기 렌더링
- 지형 충돌 자동 처리
- 홀드 해제로 즉시 구조물 저장

**새로운 클래스:**
```java
- HoldInputHandler.java (홀드 입력 처리)
- RegionPositionCalculator.java (위치 계산)
- PreviewRenderer.java (실시간 미리보기)
- CapsuleClientEvents.java (클라이언트 이벤트)
```

### Phase 3: 캡슐 타입 및 GUI (3-4주)
**목표**: 다양한 캡슐 타입과 사용자 인터페이스

**구현 기능:**
- 캡슐 타입별 크기 제한 (Small, Medium, Large)
- 캡슐 내용물 미리보기 GUI
- 캡슐 이름 설정 및 설명 추가
- 저장된 구조물 정보 표시

**캡슐 타입:**
- **Small Capsule**: 8x8x8 (개인용 소형 건물)
- **Medium Capsule**: 16x16x16 (일반 주택)
- **Large Capsule**: 32x32x32 (대형 건축물)

### Phase 4: 고급 기능 (4-5주)
**목표**: 실용성을 높이는 고급 기능들

**구현 기능:**
- 배치 미리보기 시스템 (반투명 홀로그램)
- 충돌 감지 및 경고
- 회전 기능 (90도 단위)
- 블록 엔티티 데이터 보존 (상자 내용물 등)

### Phase 5: 특수 기능 및 최적화 (3-4주)
**목표**: 성능 최적화 및 특수 기능

**구현 기능:**
- 엔티티 저장 (동물, 몹 포함)
- 구조물 압축 알고리즘
- 멀티플레이어 동기화
- 성능 최적화

## 🏗️ 기술적 구현 사항

### 핵심 아키텍처

**데이터 구조:**
```java
public class StructureData {
    private Map<BlockPos, BlockState> blocks;           // 블록 데이터
    private Map<BlockPos, CompoundTag> blockEntities;   // 블록 엔티티
    private List<EntityData> entities;                  // 엔티티 데이터
    private BlockPos dimensions;                        // 구조물 크기
    private String name;                                // 구조물 이름
    private String description;                         // 설명
    private UUID creator;                               // 제작자
    private long timestamp;                             // 생성 시간
}
```

**NBT 저장 형식:**
```json
{
  "structure_data": {
    "dimensions": [16, 16, 16],
    "blocks": {...},
    "block_entities": {...},
    "entities": [...],
    "metadata": {
      "name": "My House",
      "description": "A cozy home",
      "creator": "player-uuid",
      "timestamp": 1640995200000
    }
  }
}
```

### 주요 시스템들

**1. 홀드 기반 구조물 캡처 시스템**
```java
public class HoldBasedCapture {
    public static void startHolding(Player player, ItemStack capsule) {
        // 홀드 시작, 실시간 미리보기 활성화
        // 시선 방향 기반 영역 위치 계산
        // 캡슐 크기에 맞는 영역 설정
    }
    
    public static StructureData captureOnRelease(Level world, BoundingBox region) {
        // 홀드 해제 시 즉시 구조물 저장
        // 블록 스캔 및 데이터 수집
        // 압축 및 최적화
    }
}
```

**2. 구조물 배치 시스템**
```java
public class StructureDeployment {
    public static boolean deployStructure(Level world, BlockPos pos, StructureData data) {
        // 충돌 검사
        // 단계별 블록 배치 (TileEntity → 일반 블록 → 엔티티)
        // 애니메이션 효과
    }
}
```

**3. 실시간 미리보기 시스템**
```java
public class PreviewRenderer {
    @OnlyIn(Dist.CLIENT)
    public static void renderHoldPreview(PoseStack poseStack, BoundingBox region, BlockPos pos) {
        // 홀드 중 실시간 영역 표시
        // 반투명 박스 렌더링
        // 충돌 블록 강조 표시
        // 거리 조정 시 실시간 업데이트
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void renderDeployPreview(PoseStack poseStack, StructureData data, BlockPos pos) {
        // 배치 전 미리보기
        // 반투명 블록 렌더링
    }
}
```

## 🎨 아이템 및 레시피

### 아이템 목록

**기본 재료:**
- **캡슐 코어**: 기본 캡슐 제작 재료
- **확장 칩**: 캡슐 용량 업그레이드용

**캡슐 타입:**
- **Small Capsule**: 기본 캡슐 (8x8x8)
- **Medium Capsule**: 중형 캡슐 (16x16x16)  
- **Large Capsule**: 대형 캡슐 (32x32x32)
- **Creative Capsule**: 크리에이티브 전용 무제한 캡슐

**도구:**
- **Capsule Scanner**: 구조물 정보 확인 도구 (향후 추가 예정)
- **주요 변경**: 캡슐 완드 제거, 캡슐만으로 홀드 기반 선택

### 제작 레시피

**캡슐 코어**
```
[R] [D] [R]
[D] [E] [D]
[R] [D] [R]

R = 레드스톤, D = 다이아몬드, E = 엔더펄
```

**Small Capsule**
```
[I] [C] [I]
[C] [G] [C]
[I] [C] [I]

I = 철괴, C = 캡슐 코어, G = 유리
```

## 🎮 사용자 경험 (UX)

### 기본 워크플로우

1. **준비 단계**
   - 캡슐 제작 (완드 불필요)
   - 저장할 구조물 준비

2. **구조물 저장 (홀드 방식)**
   - 캡슐을 들고 우클릭 홀드 시작
   - 플레이어 앞에 영역이 실시간 표시
   - 시선 움직임으로 영역 위치 조정
   - 마우스 휠로 거리 조정
   - 홀드 해제로 즉시 저장

3. **구조물 배치**
   - 원하는 위치에서 캡슐 우클릭
   - 미리보기 확인 (Shift+우클릭)
   - 회전 조정 (R키)
   - 최종 배치 확인

### GUI 설계

**캡슐 정보 창**
- 구조물 이름 및 설명
- 크기 정보 (16x12x16)
- 포함된 블록 수 (1,234개)
- 제작자 및 생성 시간
- 미리보기 이미지

**설정 창**
- 배치 모드 선택 (덮어쓰기/공간 확인)
- 회전 각도 조절
- 높이 오프셋 설정

## ⚙️ 기술 스펙

### 성능 고려사항

**메모리 최적화:**
- 구조물 압축 알고리즘 적용
- 사용하지 않는 데이터 지연 로딩
- 대용량 구조물 청크 단위 처리

**네트워크 최적화:**
- 구조물 데이터 압축 전송
- 점진적 블록 배치로 지연 방지
- 클라이언트-서버 동기화 최적화

## 🧪 테스트 계획

### 단위 테스트
- 구조물 저장/로드 정확성
- NBT 데이터 무결성
- 영역 선택 알고리즘

### 성능 테스트
- 대용량 구조물 처리 성능
- 메모리 사용량 모니터링
- 서버 TPS 영향도 측정

## 📚 학습 목표

이 모드를 개발하면서 습득할 수 있는 기술들:

**초급 단계:**
- NeoForge 모드 구조 이해
- 아이템 생성 및 NBT 데이터 처리
- 기본 이벤트 핸들링

**중급 단계:**
- GUI 시스템 구현
- 클라이언트-서버 통신
- 복잡한 데이터 구조 설계

**고급 단계:**
- 렌더링 시스템 커스터마이징
- 성능 최적화 기법
- 멀티플레이어 동기화

## 🚧 개발 리스크 및 대응방안

**기술적 리스크:**
- **대용량 구조물 성능 이슈**
  - 대응: 청크 단위 처리, 비동기 로딩
- **멀티플레이어 동기화 문제**
  - 대응: 단계적 배치, 충돌 검사 강화

**사용성 리스크:**
- **복잡한 UI로 인한 사용자 혼란**
  - 대응: 단계별 튜토리얼, 직관적 UI 설계
- **의도치 않은 구조물 덮어쓰기**
  - 대응: 확인 대화상자, 백업 시스템

## 📅 개발 일정

**총 개발 기간**: 약 16-20주

| 단계 | 기간 | 주요 작업 |
|------|------|-----------|
| Phase 1 | 3주 | 기본 캡슐 시스템 |
| Phase 2 | 3주 | 홀드 기반 영역 선택 시스템 |
| Phase 3 | 4주 | 캡슐 타입 & GUI |
| Phase 4 | 5주 | 고급 기능 구현 |
| Phase 5 | 4주 | 최적화 & 특수 기능 |
| 버그 수정 | 1-2주 | 안정성 개선 |

## 🎯 성공 지표

**기능적 지표:**
- [x] 구조물 저장/배치 100% 정확도
- [x] 32x32x32 크기까지 안정적 처리
- [x] 멀티플레이어 환경에서 정상 동작

**성능 지표:**
- [x] 16x16x16 구조물 1초 내 배치
- [x] 서버 TPS 5% 이하 영향
- [x] 메모리 사용량 50MB 이하

**사용자 경험:**
- [x] 5분 내 기본 사용법 학습 가능
- [x] 직관적인 UI/UX
- [x] 안정적인 오류 처리

---

**최종 목표**: 마인크래프트 건축의 새로운 패러다임을 제시하는, 실용적이고 재미있는 모드 완성