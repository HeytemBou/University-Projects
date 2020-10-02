<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use Hash;
use JWTAuth;
  
class APIController extends Controller
{

 public function register(Request $request) {
 $input = $request->all();
 $input['password'] = Hash::make($input['password']);
 User::create($input);
 return response()->json(['result'=>true]);

    }


 public function login(Request $request) {
 $input = $request->all();
 if (!$token = JWTAuth::attempt($input)) {
 return response()->json(['result' => 'wrong email or password.']);
      } 
 return response()->json(['result' => $token]);
 
     }
     

 public function get_user_details(Request $request){
 $input = $request->all();
 $user = JWTAuth::toUser($input['token']);
 return response()->json(['result' => $user]);
 }

 public function getAuthUser(Request $request){
     $user = JWTAuth::toUser($request ->header('token'));
     return response()->json(['result'=> $user]);
 }

 public function deleteUserAccount(Request $request){
    $id_user = $request->input('id_user');
    User::where('id', '=',$id_user )
    ->delete();
 }

 public function getProfileInfos(Request $request){

    $user_id = $request->input('id_user');
    echo  User::where('id', '=',$user_id )->get();
 }

 public function updateUserInfos(Request $request){
    $id = $request->input('id_user');
    $email = $request->input('email');
    $username = $request->input('username');
    $user = User::find($id);
    $user->email =$email ;
    $user->name =$username ;
    $user->save();

 }



}


