<?php

namespace App\Http\Controllers;

use App\sous_categorie;
use Illuminate\Http\Request;

class SousCategorieController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
        $id = $request->input('id');
        //$subcategories = sous_categorie::find(2);

        $subcategories = sous_categorie::where('ID_categorie', '=',$id )->take(100)->get();;
        echo $subcategories ;
    }

    public function getSubCategoryInfos(Request $request){
        $id = $request->input('sub_cat_id');
        echo sous_categorie::where('ID_sous_categorie','=',$id)->get();

    }
    public function getSubCategoriesFullList(){
        echo sous_categorie::all();

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
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\sous_categorie  $sous_categorie
     * @return \Illuminate\Http\Response
     */
    public function show(sous_categorie $sous_categorie)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\sous_categorie  $sous_categorie
     * @return \Illuminate\Http\Response
     */
    public function edit(sous_categorie $sous_categorie)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\sous_categorie  $sous_categorie
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, sous_categorie $sous_categorie)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\sous_categorie  $sous_categorie
     * @return \Illuminate\Http\Response
     */
    public function destroy(sous_categorie $sous_categorie)
    {
        //
    }
}
