<?php

namespace App\Http\Controllers;

use App\commentaire;
use Illuminate\Http\Request;

class CommentaireController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    { 
        $ad_id = $request->input('AdId');
        $comments_list = commentaire::join('users', 'users.id', '=', 'commentaires.ID_user')
        ->where('ID_annonce', '=',$ad_id )
        ->get();
        echo $comments_list ;
        
    }
    public function deleteComment(Request $request){

       

    }
    public function editComment(Request $request){

    }
    public function addNewComment(Request $request){
        $new_comment = new commentaire ;
        $new_comment->ID_annonce= $request->input('AdId');
        $new_comment->ID_user = $request->input('UserId');
        $new_comment->Contenu = $request->input('CommentContent');
        $new_comment->DatePublication = $request->input('CommentPublicationDate');
        $new_comment->save();
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
     * @param  \App\commentaire  $commentaire
     * @return \Illuminate\Http\Response
     */
    public function show(commentaire $commentaire)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\commentaire  $commentaire
     * @return \Illuminate\Http\Response
     */
    public function edit(commentaire $commentaire)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\commentaire  $commentaire
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, commentaire $commentaire)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\commentaire  $commentaire
     * @return \Illuminate\Http\Response
     */
    public function destroy(commentaire $commentaire)
    {
        //
    }
}
