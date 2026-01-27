# 🛒 Ecommerce AI-Integrated Platform
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:70e1f5,100:ffd194&height=200&section=header&text=Ecommerce&fontSize=50" />

## 📖 프로젝트 소개
이 프로젝트는 **Spring Boot 기반의 백엔드**와 **FastAPI 기반의 AI 서버**를 결합한 **지능형 이커머스 플랫폼**입니다.  
사용자의 질문을 AI가 이해하고, 필요한 상품 정보를 직접 찾아 SQL 쿼리를 생성하거나 추천 결과를 반환합니다.

쉽게 말해, **상품을 직접 검색하는 대신 AI에게 물어볼 수 있는 쇼핑 플랫폼**을 만드는 것이 목표였습니다.

---

## 🏗 시스템 구조
![Architecture Diagram](./architecture.jpeg)

| Category | Technology |
| :--- | :--- |
| **Infra** | AWS EC2, Docker |
| **Back-end** | Spring Boot 3.5.6, Redis |
| **AI Server** | FastAPI, LangChain, LLM |
| **Database** | MySQL, Chroma Vector DB |

---

## 🚀 주요 기능

### 💡 백엔드 (Spring Boot)
- **Docker 기반 배포**로 환경 일관성과 확장성을 확보했습니다.  
- **Redis 캐싱**을 적용해 자주 조회되는 데이터를 빠르게 제공합니다.  
- **MySQL**로 사용자, 상품, 주문 등 핵심 이커머스 데이터를 구조적으로 관리합니다.

### 🧠 AI 서버 (FastAPI + LangChain) 
- 텍스트를 벡터로 변환해 **사용자 질문을 분석**하여 의미 기반 검색을 수행합니다.  
- **LangChain**을 이용하여 질문의 의도에 맞는 SQL 쿼리나 응답을 생성합니다.  
- 관련도가 높은 상품 정보를 **Chroma 벡터 유사도 검색**으로 빠르게 탐색합니다.  
- 검색 결과를 바탕으로 실행 가능한 SQL 쿼리문을 동적으로 생성하여 백엔드로 전달합니다.
  
[AI Server repo](https://github.com/zzichu/ecomerce-ai)

---

## ⚙️ 동작 흐름
1. 사용자가 백엔드로 질문을 보냅니다.  
2. 백엔드는 질문을 AI 서버로 전달합니다.  
3. AI 서버는 질문을 임베딩하고, Chroma Vector DB에서 관련 데이터를 탐색합니다.  
4. LangChain과 LLM이 검색 결과를 분석해 SQL 쿼리 또는 자연어 응답을 생성합니다.  
5. 백엔드는 실행된 쿼리 결과를 가공해 사용자에게 최종 응답을 반환합니다.

---

## 🌟 개발 의도
Text to SQL, **자연어를 통해 상품을 검색하는 서비스**를 만들고 싶었습니다.  
AI가 백엔드 로직과 직접 통신하며,  
데이터 조회부터 응답 생성까지 모두 자동화되는 구조를 실험했습니다.

