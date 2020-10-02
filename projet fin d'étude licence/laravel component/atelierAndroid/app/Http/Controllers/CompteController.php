<?php

namespace App\Http\Controllers;

use App\compte;
use Illuminate\Http\Request;

class CompteController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        
               $compte = new compte;
               $compte->Username =$request->input('username');
               $compte->Password = $request->input('password');
               $compte->Email = $request->input('email');
               $compte->save();
               
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\compte  $compte
     * @return \Illuminate\Http\Response
     */
    public function show(compte $compte)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\compte  $compte
     * @return \Illuminate\Http\Response
     */
    public function edit(compte $compte)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\compte  $compte
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, compte $compte)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\compte  $compte
     * @return \Illuminate\Http\Response
     */
    public function destroy(compte $compte)
    {
        //
    }
}
