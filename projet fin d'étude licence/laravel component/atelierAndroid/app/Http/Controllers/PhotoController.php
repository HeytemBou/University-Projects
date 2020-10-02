<?php

namespace App\Http\Controllers;

use App\photo;
use Illuminate\Http\Request;

class PhotoController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
        $id = $request->input('id_user');
        echo  $image_resource = photo::where('id_user', '=',$id )->get();
       
        
    }

    public function retrieveAllPhotos(){
        echo photo::where('id_user','=',null)->get();
    }

    public function getAdPhotos(Request $request){
        $id = $request->input('ad_id');
        echo photo::where('id_annonce','=',$id)->get()->first();

        
    }
    public function getAdTriplePhotos(Request $request){
        $id = $request->input('ad_id');
        echo photo::where('id_annonce','=',$id)->get();

        
    }

    public function retrieveSingleProfilePhoto(Request $request){
        $user_id = $request->input('user_id');
        echo photo::where('id_user','=',$user_id)->get();

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
        $new_photo = new photo ;
        $new_photo->Resource = $request->input('ImageString');
        $new_photo->id_user = $request->input('profile_id');
        $new_photo->id_annonce = $request->input('ad_id');
        $new_photo->save();
    }

    public function updateProfilePhoto(Request $request)
    {
        $p = $request->input('ImageString');
        $id =$request->input('user_id');
       
            photo::where('id_user',$id)
            ->update(['Resource'=> $p]);

    }

    public function deleteAdPhotos(Request $request){
        

    }


    /**
     * Display the specified resource.
     *
     * @param  \App\photo  $photo
     * @return \Illuminate\Http\Response
     */
    public function show(photo $photo)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\photo  $photo
     * @return \Illuminate\Http\Response
     */
    public function edit(photo $photo)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\photo  $photo
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, photo $photo)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\photo  $photo
     * @return \Illuminate\Http\Response
     */
    public function destroy(photo $photo)
    {
        //
    }
}
