<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateAnnoncesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('annonce', function (Blueprint $table) {
            $table->engine = 'InnoDB';
            $table->increments('ID_annonce');
            $table->integer('ID_sous_categorie')->unsigned();
            $table->integer('ID_user')->unsigned();
            $table->String('Titre');
            $table->enum('Type',['Offre','Demande']);
            $table->String('Description');
            $table->date('DatePublication');
            $table->double('Longitude');
            $table->double('Latitude');
            $table->String('Address');
            $table->String('Nom');
            $table->String('Prenom');
            $table->String('Telephone');
            $table->String('Email');
            $table->foreign('ID_sous_categorie')->references('ID_sous_categorie')->on('sous_categorie')->onDelete('cascade')->onUpdate('cascade');
            $table->foreign('ID_user')->references('id')->on('users')->onDelete('cascade')->onUpdate('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('annonce');
        Schema::dropIfExists('photos');
    }
}
