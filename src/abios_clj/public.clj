(ns abios-clj.public
  (:require [clj-http.client :as http]))

(def games-keyword (keyword "games[]"))

;; games are integer ids
;; `(map (juxt :title :id) (abios-clj.core/games))`


(defn- abios-ajax-get
  ([url]
   (abios-ajax-get url {}))
  ([url query-params]
   (:body (http/get url {:query-params query-params
                         :as :json}))))

(defn upcoming-games
  ([]
   (upcoming-games {}))
  ([{:keys [take games]
     :or {take 10}}]
   (abios-ajax-get "https://abiosgaming.com/ajax/matches"
                   {:upcoming true
                    :take take
                    games-keyword games})))

(defn live-games
  ([]
   (live-games {}))
  ([{:keys [games]}]
   (abios-ajax-get "https://abiosgaming.com/ajax/matches"
                   {:live true
                    games-keyword games})))

(defn past-games
  ([]
   (past-games {}))
  ([{:keys [take games]
     :or {take 10}}]
   (abios-ajax-get "https://abiosgaming.com/ajax/matches"
                   {:past true
                    :take take
                    games-keyword games})))
