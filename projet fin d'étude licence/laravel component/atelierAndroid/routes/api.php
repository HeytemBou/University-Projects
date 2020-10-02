<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});
Route::middleware ( ['api'])->group(function () {
    Route::post('register', 'APIController@register');
    Route::post('login', 'APIController@login');
    Route::get('RetrieveCategories', 'CategorieController@index');
    Route::post('RetrieveSubCategories', 'SousCategorieController@index');
    Route::post('SaveProfileImage', 'PhotoController@store');
    Route::post('UpdateProfilePhoto', 'PhotoController@updateProfilePhoto');
    Route::post('LoadProfileImage', 'PhotoController@index');
    Route::post('PublishAd', 'AnnonceController@store');
    Route::get('GetFullAdsListWithPhotos', 'AnnonceController@index');
    Route::get('GetFullAdsList', 'AnnonceController@RetrieveAllAds');

    Route::post('RetrieveSingleProfilePhoto', 'PhotoController@retrieveSingleProfilePhoto');

    //returns the coordinates of all ads in the database
    Route::get('GetCoordinates', 'AnnonceController@RetrieveAllCoordinates');
    //retrieve all images in the database
    Route::get('GetAllPhotos', 'PhotoController@RetrieveAllPhotos');

    //returns the details of a specific ad 
    Route::post('GetAdDetails', 'AnnonceController@getAdDetails');
    //returns list of ads created by a specific user
    Route::post('GetUserCreatedAds','AnnonceController@getUserCreatedAds');
    //returns the images of a specific ad
    Route::post('GetAdPhotos', 'PhotoController@getAdPhotos');

    Route::post('GetAdTriplePhotos', 'PhotoController@getAdTriplePhotos');
     //returns the user created ads
    Route::post('GetUserCreatedAdsOnly', 'AnnonceController@getUserCreatedAdsOnly');



    //get sub category infos
    Route::post('GetSubCatInfos', 'SousCategorieController@getSubCategoryInfos');
    //get category infos
    Route::post('GetCatInfos', 'CategorieController@getCategoryInfos');
     // update user account informations as well their profile photo
    Route::post('UpdateUserInfos', 'APIController@updateUserInfos');
    Route::post('UpdateUserProfilePhoto', 'APIController@updateUserProfilePhoto');

    Route::post('GetProfileInfos', 'APIController@getProfileInfos');

    

    //filter the ads list 
    Route::post('FilterAdsList', 'AnnonceController@filterAdsList');

    //delete user account + profile image + personal ads and their corresponding photos
    


    //delete user created ad
    Route::post('DeleteUserCreatedAd', 'AnnonceController@deleteUserCreatedAd');

    //routes for comments handling
    Route::post('RetrieveCommentsList', 'CommentaireController@index');
    Route::post('AddNewComment', 'CommentaireController@addNewComment');
    Route::post('DeleteUserCreatedAd', 'AnnonceController@deleteUserCreatedAd');
    Route::post('DeleteUserCreatedAd', 'AnnonceController@deleteUserCreatedAd');
    Route::post('DeleteUserCreatedAd', 'AnnonceController@deleteUserCreatedAd');

    //routes for updating user created ads 
    Route::post('UpdateUserCreatedAd', 'AnnonceController@updateUserCreatedAd');

    //routes for account deletion
    Route::post('DeleteUserAccount', 'APIController@deleteUserAccount');
    Route::post('GetUserCreatedAdsIds', 'AnnonceController@getUserCreatedAdsIds');
   Route::post('DeleteAd', 'AnnonceController@deleteAd');
   Route::post('GetUserCreatedAdsNumber', 'AnnonceController@getUserCreatedAdsNumber');


   
   Route::get('GetSubCategoriesFullList', 'SousCategorieController@getSubCategoriesFullList');
 
    

    //responisble for handling authentication
    Route::group(['middleware' => 'jwt-auth'],function () {
    Route::post('get_user_details', 'APIController@get_user_details');
    Route::post('getAuthUser', 'APIController@getAuthUser');
   

    });
  });
   