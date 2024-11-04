# OpenJDK 17 이미지를 기반으로 사용
FROM openjdk:17-jdk-alpine

# ARG에서 받은 PROFILE을 ENV 설정을 통해 컨테이너 실행 시 환경 변수로 전달
ARG PROFILE
ENV PROFILE=${PROFILE}

# 프로파일 인수가 없을 경우 에러 발생
RUN if [ -z "$PROFILE" ]; then echo "PROFILE build argument is required"; exit 1; fi

# 작업 디렉토리 설정
WORKDIR /app

# 애플리케이션 JAR 파일과 통합된 설정 파일 복사
COPY ./build/libs/hid.jar app.jar
COPY ./src/main/resources/application.yml /app/application.yml

# 지정된 프로파일로 애플리케이션 실행
CMD ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "app.jar"]