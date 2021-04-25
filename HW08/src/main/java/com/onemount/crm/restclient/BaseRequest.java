package com.onemount.crm.restclient;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@Headers("Accept: application/json")
interface BaseRequest<V, VPOJO> {

  @RequestLine("GET /")
  List<V> list();

  @RequestLine("GET /{id}")
  V get(@Param("id") long key);

  @Headers("Content-Type: application/json")
  @RequestLine("POST /")
  V post(VPOJO value);

  @Headers("Content-Type: application/json")
  @RequestLine("PUT /{id}/")
  V put(@Param("id") long id, VPOJO value);


  @RequestLine("DELETE /{id}/")
  long delete(@Param("id") long id);
}