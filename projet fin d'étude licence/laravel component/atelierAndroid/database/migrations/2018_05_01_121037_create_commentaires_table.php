<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateCommentairesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('commentaires', function (Blueprint $table) {
            $table->increments('id');
            $table->engine = 'InnoDB';
            $table->String('Contenu');
            $table->integer('ID_user')->unsigned();
            $table->integer('ID_annonce')->unsigned();
            $table->date('DatePublication');
            $table->foreign('ID_user')->references('id')->on('users')->onDelete('cascade')->onUpdate('cascade');
            $table->foreign('ID_annonce')->references('ID_annonce')->on('annonce')->onDelete('cascade')->onUpdate('cascade');
           
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('commentaires');
    }
}
