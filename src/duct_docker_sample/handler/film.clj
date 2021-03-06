(ns duct-docker-sample.handler.film
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [duct-docker-sample.boundary.film :as boundary.film]
            [duct-docker-sample.views.film :as views.film]
            [integrant.core :as ig]))

(defmethod ig/init-key :duct-docker-sample.handler.film/show-create [_ _]
  (fn [_]
    [::response/ok (views.film/create-film-view)]))

(defn- film-form->film
  [film-form]
  (as-> film-form film
    (dissoc film "__anti-forgery-token")
    (reduce-kv (fn [m k v] (assoc m (keyword k) v))
               {}
               film)
    (update film :rating #(Integer/parseInt %))))

(defmethod ig/init-key :duct-docker-sample.handler.film/create [_ {:keys [db]}]
  (fn [{[_ film-form] :ataraxy/result :as request}]
    (let [film (film-form->film film-form)
          result (boundary.film/create-film db film)
          alerts (if (:id result)
                   {:messages ["Film added"]}
                   result)]
      [::response/ok (views.film/film-view film alerts)])))

(defmethod ig/init-key :duct-docker-sample.handler.film/list [_ {:keys [db]}]
  (fn [_]
    (let [films-list (boundary.film/list-films db)]
      (if (seq films-list)
        [::response/ok (views.film/list-films-view films-list {})]
        [::response/ok (views.film/list-films-view [] {:messages ["No films found."]})]))))