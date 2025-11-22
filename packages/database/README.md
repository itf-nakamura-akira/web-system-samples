# データベース (`database`)

このパッケージは、Webアプリケーションのデータベース関連のファイルを管理します。
[node-pg-migrate](https://github.com/salsita/node-pg-migrate) を使用して、PostgreSQLのマイグレーションを管理します。

## 主なコマンド

### マイグレーションファイルの作成

新しいマイグレーションファイルを作成します。

```bash
bunx node-pg-migrate create create-hoge-table -j ts
```

### マイグレーションの実行 (Up)

最新のマイグレーションを適用します。

```bash
bun run up
```

### マイグレーションのロールバック (Down)

最後のマイグレーションを取り消します。

```bash
bun run down
```

### 開発用データの挿入

開発用の初期データをデータベースに挿入します。

```bash
bun run dev-data:insert
```

### 開発用データの削除

データベース内のデータをすべて削除します。

```bash
bun run dev-data:delete
```
