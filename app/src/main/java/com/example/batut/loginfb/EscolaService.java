package com.example.batut.loginfb;

import com.example.batut.loginfb.Escola;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by batut on 01/11/2017.
 */

public interface EscolaService {
    @GET("{nome}")
    Call<Escola> getEscola(@Path("nome") String nome);
}
