const guideData = {
  theft: {
    title: "절도 예방 가이드",
    desc: "여행 중 절도 피해를 예방하기 위한 수칙입니다.",
    items: [
      {
        icon: "briefcase",
        title: "소지품 관리",
        text: "가방은 몸 앞쪽으로 메고, 지갑과 휴대폰은 분산 보관하세요."
      },
      {
        icon: "hotel",
        title: "숙소 관리",
        text: "숙소 외출 시 귀중품은 안전한 곳에 보관하고 문단속을 확인하세요."
      },
      {
        icon: "eye",
        title: "의심 행동 주의",
        text: "주변에 수상한 사람이 있거나 접근이 반복되면 즉시 자리를 피하세요."
      }
    ]
  },

  violence: {
    title: "폭력 예방 가이드",
    desc: "야간 이동 및 유흥가 주변에서의 안전 수칙입니다.",
    items: [
      {
        icon: "moon",
        title: "늦은 시간 이동 주의",
        text: "늦은 시간에는 골목길보다 사람이 많은 대로변을 이용하세요."
      },
      {
        icon: "message-circle-x",
        title: "시비 상황 회피",
        text: "언쟁이나 시비가 발생하면 대응하지 말고 즉시 거리를 두세요."
      },
      {
        icon: "phone-call",
        title: "도움 요청",
        text: "위험을 느끼면 주변 상점, 경찰서, 112에 즉시 도움을 요청하세요."
      }
    ]
  },

  sexual: {
    title: "성범죄 예방 가이드",
    desc: "여행 중 성범죄 위험 상황을 예방하기 위한 수칙입니다.",
    items: [
      {
        icon: "users",
        title: "단독 이동 주의",
        text: "늦은 시간 낯선 장소에서는 혼자 이동하지 않는 것이 좋습니다."
      },
      {
        icon: "map-pin",
        title: "위치 공유",
        text: "숙소 복귀 시 지인에게 현재 위치나 이동 경로를 공유하세요."
      },
      {
        icon: "shield-alert",
        title: "위험 신호 대응",
        text: "불쾌한 접근이나 위협을 느끼면 즉시 주변 사람에게 도움을 요청하세요."
      }
    ]
  },

  pickpocket: {
    title: "소매치기 예방 가이드",
    desc: "관광지와 혼잡한 장소에서의 소매치기 예방 수칙입니다.",
    items: [
      {
        icon: "backpack",
        title: "가방 위치 확인",
        text: "혼잡한 곳에서는 가방을 앞으로 메고 지퍼가 닫혀 있는지 확인하세요."
      },
      {
        icon: "wallet",
        title: "귀중품 분산 보관",
        text: "현금, 카드, 여권은 한 곳에 보관하지 말고 나누어 보관하세요."
      },
      {
        icon: "train",
        title: "대중교통 주의",
        text: "지하철, 버스, 시장 등 사람이 많은 장소에서는 소지품을 가까이 두세요."
      }
    ]
  },

  night: {
    title: "야간 안전 수칙",
    desc: "밤 시간대 안전한 이동을 위한 기본 수칙입니다.",
    items: [
      {
        icon: "route",
        title: "큰길 이용",
        text: "어두운 골목이나 인적이 드문 길보다 밝은 대로변을 이용하세요."
      },
      {
        icon: "battery-charging",
        title: "휴대폰 배터리 확인",
        text: "야간 이동 전 휴대폰 배터리를 충분히 확보하세요."
      },
      {
        icon: "navigation",
        title: "경로 확인",
        text: "목적지까지의 경로와 주변 경찰서 위치를 미리 확인하세요."
      }
    ]
  },

  traffic: {
    title: "교통사고 예방 가이드",
    desc: "낯선 여행지에서 교통사고를 예방하기 위한 수칙입니다.",
    items: [
      {
        icon: "traffic-cone",
        title: "횡단보도 이용",
        text: "낯선 지역에서는 무단횡단을 피하고 신호를 반드시 확인하세요."
      },
      {
        icon: "car",
        title: "렌터카 주의",
        text: "렌터카 이용 시 지역 도로 상황과 주차 규정을 미리 확인하세요."
      },
      {
        icon: "bike",
        title: "킥보드·자전거 주의",
        text: "공유 킥보드나 자전거 이용 시 헬멧 착용과 보행자 주의를 지키세요."
      }
    ]
  },

  etc: {
    title: "기타 안전 가이드",
    desc: "여행 중 예상치 못한 상황에 대비하기 위한 수칙입니다.",
    items: [
      {
        icon: "cloud-rain",
        title: "날씨 확인",
        text: "여행 전 기상 상황을 확인하고 우천·폭염·한파에 대비하세요."
      },
      {
        icon: "file-text",
        title: "신분증 보관",
        text: "신분증과 예약 정보를 안전하게 보관하고 사본을 준비하세요."
      },
      {
        icon: "circle-help",
        title: "긴급 연락처 확인",
        text: "112, 119, 숙소 연락처 등 긴급 연락처를 미리 저장하세요."
      }
    ]
  }
};

const tabs = document.querySelectorAll(".tab");
const guideTitle = document.querySelector("#guideTitle");
const guideDesc = document.querySelector("#guideDesc");
const guideList = document.querySelector("#guideList");

function renderGuide(type) {
  const data = guideData[type];

  guideTitle.textContent = data.title;
  guideDesc.textContent = data.desc;

  guideList.innerHTML = data.items.map(item => `
    <article class="guide-item">
      <div class="guide-icon">
        <i data-lucide="${item.icon}"></i>
      </div>

      <div>
        <h3>${item.title}</h3>
        <p>${item.text}</p>
      </div>
    </article>
  `).join("");

  lucide.createIcons();
}

tabs.forEach(tab => {
  tab.addEventListener("click", () => {
    tabs.forEach(btn => btn.classList.remove("active"));
    tab.classList.add("active");

    const type = tab.dataset.type;
    renderGuide(type);
  });
});

renderGuide("theft");