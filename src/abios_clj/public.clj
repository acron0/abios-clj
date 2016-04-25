(ns abios-clj.public
  (:require [clj-http.client :as http]))

(def games-keyword (keyword "games[]"))

(def matches-endpoint "https://abiosgaming.com/ajax/matches")

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
   (let [args {:upcoming true
               :take take}
         args (if (not-empty games) (assoc args games-keyword games) args)]
     (abios-ajax-get matches-endpoint args))))

(defn live-games
  ([]
   (live-games {}))
  ([{:keys [games]}] ;; "take" doesnt work
   (let [args {:live true}
         args (if (not-empty games) (assoc args games-keyword games) args)]
     (abios-ajax-get matches-endpoint args))))

(defn past-games
  ([]
   (past-games {}))
  ([{:keys [take games]
     :or {take 10}}]
   (let [args {:past true
               :take take}
         args (if (not-empty games) (assoc args games-keyword games) args)]
     (abios-ajax-get matches-endpoint args))))
