<?php

function insert_header_top() {
  require_once "header_top.php";
}

function insert_header_bottom($body_class = NULL) {
  require_once "header_bottom.php";
}

function insert_header($body_class = NULL) {
  insert_header_top();
  insert_header_bottom($body_class);
}

function insert_footer_top() {
  require_once "footer_top.php";
  require_once "footer_bottom.php";
}

function insert_footer_bottom() {
  require_once "footer_bottom.php";
}

function insert_footer() {
  insert_footer_top();
  insert_footer_bottom();
}