FROM postgres:18

# ロケールのインストール
RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8

# 初期化スクリプトのコピー
COPY init.sql /docker-entrypoint-initdb.d/
