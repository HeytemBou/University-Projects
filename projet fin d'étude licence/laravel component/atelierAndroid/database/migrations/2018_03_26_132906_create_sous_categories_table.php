<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSousCategoriesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('sous_categorie', function (Blueprint $table) {
            $table->engine = 'InnoDB';
            $table->increments('ID_sous_categorie');
            $table->integer('ID_categorie')->unsigned();
            $table->String('name');
            $table->text('description');
            $table->foreign('ID_categorie')->references('ID_categorie')->on('categorie')->onDelete('cascade')->onUpdate('cascade');
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
        Schema::dropIfExists('sous_categorie');
    }

}
