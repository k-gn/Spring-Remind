
# API Server Error 처리

1. 통일된 Error Response를 가져야하는 이유
   - status에 따라 응답값이 계속 바뀐다면 클라이언트 입장에서 사용하기 힘들다.
   - 보안상이나 다양한 이슈로 시스템 에러 메시지를 API로 내보내면 안된다. => 항상 정제된 메시지를 내려줘야 한다.  
2. @ControllerAdvice를 활용한 일관된 예외 핸들링
3. 클라이언트에게 Error Message를 어떻게 노출 시켜야할까?
4. 계층화를 통한 Business Exception 처리 방법
5. 효율적인 Validation