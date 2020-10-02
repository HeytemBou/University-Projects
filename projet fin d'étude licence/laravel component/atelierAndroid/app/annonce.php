<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class annonce extends Model
{
    public $table = "annonce";
    public $timestamps = false;
    protected $primaryKey = 'ID_annonce';
}
