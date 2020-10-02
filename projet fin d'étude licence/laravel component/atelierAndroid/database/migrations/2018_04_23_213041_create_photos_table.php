<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatePhotosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('photos', function (Blueprint $table) {
            $table->engine = 'InnoDB';
            $table->increments('id');
            $table->integer('id_user')->nullable()->unique();
            $table->integer('id_annonce')->nullable();
            $table->longtext('Resource');
           // $table->foreign('id_user')->references('id')->on('users')->onDelete('cascade')->onUpdate('cascade');
           // $table->foreign('id_photo')->references('ID_annonce')->on('annonce')->onDelete('cascade')->onUpdate('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('photos');
    }
}
