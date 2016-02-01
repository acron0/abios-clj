(ns abios-clj.core
  (:require [clj-http.client :as http]
            [environ.core :refer [env]]
            [clojure.string :as str]
            [schema.core :as s]))

;; docs: http://api.abiosgaming.com/

(def session-endpoint "https://api.abiosgaming.com/v1/oauth/access_token/")
(def method-endpoint "https://api.abiosgaming.com/v1/")

(def session-atom (atom nil))

(defn reset-session!
  []
  (reset!
   session-atom
   (-> session-endpoint
       (http/post
        {:as :json
         :form-params {:grant_type "client_credentials"
                       :client_id (env :abios-client-id)
                       :client_secret (env :abios-client-secret)}})
       :body)))

(defn get-token
  []
  (:access_token @session-atom))

(defn session-valid?
  []
  (-> (get-token) nil? not))

(defn replace-keys-from-map
  [line m]
  (reduce (fn [a r]
            (str/replace
             a
             (str ":" r)
             (->> (keyword r)
                  (get m)
                  (str))))
          line
          (->> line
               (re-seq #":([a-zA-Z0-9\-_]+)")
               (map second))))

(defn abios-endpoint
  [method params]
  (str method-endpoint (replace-keys-from-map method params)))

(defn abios-method
  ([method]
   (abios-method method {}))
  ([method params]
   (if-not (session-valid?)
     (reset-session!))
   (let [route (abios-endpoint method params)]
     (:body (http/get route {:query-params {:access_token (get-token)}
                             :as :json})))))

;;;;;;;;;;;;;;

(s/defn games
  []
  (abios-method "games"))

(s/defn matches
  []
  (abios-method "matches"))

(s/defn  match
  [id :- s/Int]
  (abios-method "matches/:id" {:id id}))

(s/defn tournaments
  []
  (abios-method "tournaments"))

(s/defn tournament
  [id :- s/Int]
  (abios-method "tournaments/:id" {:id id}))

(s/defn competitors
  []
  (abios-method "competitors"))

(s/defn competitorn
  [id :- s/Int]
  (abios-method "competitors/:id" {:id id}))
