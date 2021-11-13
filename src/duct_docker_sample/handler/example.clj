(ns duct-docker-sample.handler.example
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response] 
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key :duct-docker-sample.handler/example [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok (io/resource "duct_docker_sample/handler/example/example.html")]))