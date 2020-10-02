<?php

namespace App\Http\Controllers;

use App\annonce;
use App\photo;
use Illuminate\Http\Request;

class AnnonceController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
       $ads_list =  annonce::join('photos', 'annonce.ID_annonce', '=', 'photos.id_annonce')
       ->select('annonce.*', 'photos.*')
       ->get();
       echo $ads_list ;
    }

    public function RetrieveAllAds(){
        echo annonce::all();
    }

    public function RetrieveAllCoordinates(){

        $coordinates =  annonce::join('photos', 'annonce.ID_annonce', '=', 'photos.id_annonce')
        ->select('annonce.ID_annonce', 'Longitude','Latitude','Resource','Titre','ID_sous_categorie','DatePublication','Type')
        ->get();
       echo $coordinates ;
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
        $new_ad = new annonce ;
        $new_ad->ID_user = $request->input('ID_user');
        $new_ad->Titre = $request->input('Title');
        $new_ad->Description = $request->input('Description');
        $new_ad->Type = $request->input('Type');
        $new_ad->DatePublication = $request->input('DatePublication');
        $new_ad->ID_sous_categorie = $request->input('SubCategoryId');
        $new_ad->Nom = $request->input('FirstName');
        $new_ad->Prenom = $request->input('LastName');
        $new_ad->Email = $request->input('Email');
        $new_ad->Telephone= $request->input('Telephone');
        $new_ad->Longitude = $request->input('Longitude');
        $new_ad->Latitude = $request->input('Latitude');
        $new_ad->Address = $request->input('Address');
        $new_ad->save();
        $n =$new_ad->ID_annonce;
        echo $n ;
        
    }

    public function getAdDetails(Request $request){
        $n = $request->input('AdId');
        echo  annonce::where('ID_annonce', '=',$n )->get();
    }

    public function getUserCreatedAds(Request $request){
        $n = $request->input('ID_user');
        $user_created_ads = annonce::join('photos', 'photos.id_annonce', '=', 'annonce.ID_annonce')
    ->where('annonce.ID_user', '=',$n )
    ->get();
    echo  $user_created_ads ;
    }
    public function getUserCreatedAdsOnly(Request $request){
        $n = $request->input('ID_user');
        $user_created_ads = annonce::where('ID_user','=',$n)
       ->get();
    echo  $user_created_ads ;
    }


    public function deleteUserCreatedAd(Request $request){
        $id_user = $request->input('id_user');
        $id_ad = $request->input('id_ad');
        $ad_to_delete = annonce::where('ID_annonce', '=',$id_ad )
            ->where('ID_user', '=',$id_user )
            ->delete();
         $ad_photos_to_delete = photo::where('id_annonce', '=',$id_ad )
            ->delete();
             
    }

    public function deleteAd(Request $request){
        $id_ad = $request->input('id_ad');
        annonce::where('ID_annonce', '=',$id_ad )
            ->delete();
          photo::where('id_annonce', '=',$id_ad )
            ->delete();

    }
    

    public function filterAdsList(Request $request){
        $Ad_Type =  $request->input('type');
        $Ad_SubCategory =  $request->input('sub_cat');
    
        if($Ad_Type=='Both'){
            $filteringResult = annonce::join('photos', 'photos.id_annonce', '=', 'annonce.ID_annonce')
            ->where('ID_sous_categorie', '=',$Ad_SubCategory )
            ->get();
            echo $filteringResult ;
        } else {
            $filteringResult = annonce::join('photos', 'photos.id_annonce', '=', 'annonce.ID_annonce')
            ->where('ID_sous_categorie', '=',$Ad_SubCategory )
            ->where('Type', '=',$Ad_Type )
            ->get();
            echo $filteringResult ;

        }

    }

    public function updateUserCreatedAd(Request $request){
        $id =$request->input('ID_ad');
        $nTitre = $request->input('Title');
        $nDescription = $request->input('Description');
        $nType = $request->input('Type');
        $nID_sous_categorie = $request->input('SubCategoryId');
        $nNom = $request->input('FirstName');
        $nPrenom = $request->input('LastName');
        $nEmail = $request->input('Email');
        $nTelephone= $request->input('Telephone');
        $nLongitude = $request->input('Longitude');
        $nLatitude = $request->input('Latitude');
        $nAddress = $request->input('Address');
        $userCreatedAd = annonce::find($id);
        $userCreatedAd->Titre =  $nTitre ;
        $userCreatedAd->Description =$nDescription ;
        $userCreatedAd->Type=$nType ;
        $userCreatedAd->ID_sous_categorie =$nID_sous_categorie ;
        $userCreatedAd->Nom =$nNom ;
        $userCreatedAd->Prenom = $nPrenom ;
        $userCreatedAd->Email =$nEmail ;
        $userCreatedAd->Telephone=$nTelephone ;
        $userCreatedAd->Longitude =$nLongitude ;
        $userCreatedAd->Latitude =$nLatitude ;
        $userCreatedAd->Address =$nAddress ;
        $userCreatedAd->save();
        photo::where('id_annonce', '=',$id )
        ->delete();
           

    }

    public function getUserCreatedAdsIds(Request $request){
        
        $id_user = $request->input('ID_user');
        $UserCreatedAdsIds =  annonce::select('ID_annonce')->where('ID_user', '=', $id_user)
        ->get();
        echo $UserCreatedAdsIds ;
    }

    public function getUserCreatedAdsNumber(Request $request){
        $id_user = $request->input('ID_user');
        $ads_count =annonce::where('ID_user','=', $id_user)->count();
        echo $ads_count ;
    }


   

    /**
     * Display the specified resource.
     *
     * @param  \App\annonce  $annonce
     * @return \Illuminate\Http\Response
     */
    public function show(annonce $annonce)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\annonce  $annonce
     * @return \Illuminate\Http\Response
     */
    public function edit(annonce $annonce)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\annonce  $annonce
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, annonce $annonce)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\annonce  $annonce
     * @return \Illuminate\Http\Response
     */
    public function destroy(annonce $annonce)
    {
        //
    }


}
