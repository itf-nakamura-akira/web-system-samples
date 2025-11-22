# database

## マイグレーションファイルの作成コマンド

```bash
bunx node-pg-migrate create create-hoge-table -j ts
```

## マイグレーション up

```bash
bun run up
```

## マイグレーション down

```bash
bun run down
```

## テストデータの挿入

```bash
bun run dev-data:insert
```

## テストデータの削除

```bash
bun run dev-data:delete
```
