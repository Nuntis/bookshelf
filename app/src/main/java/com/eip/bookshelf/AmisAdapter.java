package com.eip.bookshelf;

/**
 * Created by Maxime on 17/02/2016.
 */

class AmisAdapter
{
    private String _name;
    private String _id;
    private String _email;

    AmisAdapter(String name, String id, String email) {
        this._name = name;
        this._id = id;
        this._email = email;
    }

    String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }

    String get_email() {
        return _email;
    }

    public void set_email(String email) {
        this._email = email;
    }
}