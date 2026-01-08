package org.example.tamaapi.feignClient.member;


import static org.example.tamaapi.common.exception.CommonExceptionHandler.throwOriginalException;

public class MemberFallback implements MemberFeignClient{

    private final Throwable cause;

    public MemberFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Authority findAuthority(String bearerJwt) {
        throwOriginalException(cause);
        return null;
    }

}
